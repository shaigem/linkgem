package com.github.shaigem.linkgem.favicon;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.shaigem.linkgem.util.ImageUtil;
import com.github.shaigem.linkgem.util.LocationUtil;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages the icons used by this program.
 *
 * @author Ronnie T.
 */
public final class IconManager {

    /**
     * Cache holding fetched icons from websites.
     */
    private final AsyncLoadingCache<String, Image>
            iconCache =
            Caffeine.newBuilder().
                    maximumSize(256).expireAfterAccess(5, TimeUnit.MINUTES).
                    removalListener((key, value, cause) ->
                            System.out.println("Removed: " + key + " : " + value + " " + cause)).
                    buildAsync((key, executor) -> CompletableFuture.supplyAsync(() -> fetchIconFromLocation(key)));


    /**
     * Gets the icon from the cache. If the icon does not exist in the cache, it will be fetched asynchronously.
     * Only the domain of a location is cached as the icon can be reused multiple times with the same sites.
     *
     * @param location the location to fetch the icon for
     * @return the CompletableFuture
     */
    public CompletableFuture<Image> getIconForLocation(String location) {
        return iconCache.get(LocationUtil.getDomainFromLocation(location));

    }

    /**
     * Gets the default bookmark icon.
     *
     * @return the default bookmark icon
     */
    public Image getDefaultBookmarkIcon() {
        return iconCache.synchronous().get("special_default_bookmark", e ->
                new Image(IconManager.class.getResource("/images/earth.png").toExternalForm()));
    }

    /**
     * Gets the default folder icon.
     *
     * @return the default folder icon
     */
    public Image getDefaultFolderIcon() {
        return iconCache.synchronous().get("special_default_folder", e ->
                new Image(IconManager.class.getResource("/images/folder.png").toExternalForm()));
    }

    /**
     * Gets the default google icon that is displayed when google's database does not contain a certain website's favicon.
     *
     * @return the default google icon
     */
    private Image getDefaultGoogleImage() {
        return iconCache.synchronous().get("special_default_google", e ->
                new Image(IconManager.class.getResource("/images/default_google_fav.png").toExternalForm()));
    }

    /**
     * Fetches a icon from the given location.
     *
     * @param location the location to fetch a icon for
     * @return the Image object of the icon
     */
    private Image fetchIconFromLocation(final String location) {
        String iconURLString = "https://www.google.com/s2/favicons?domain_url=" + location;
        final HttpURLConnection connection;
        URL url;
        try {
            url = new URL(iconURLString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36");
            Image image = new Image(connection.getInputStream(), 0, 16, true, true);
            System.out.println("Fetching and caching icon from: " + location + ". Icon: " + iconURLString);
            if (ImageUtil.isSameImage(image, getDefaultGoogleImage())) {
                // if no favicon is found using google's service, it always gives google's default icon
                // we want to compare the images to the one we have and if they are the same, we will use our default icon instead
                return getDefaultBookmarkIcon();
            }
            return image;
        } catch (IOException e) {
            return getDefaultBookmarkIcon();
        }
    }

      /*  Document doc;

        String favIconURLString = "";

        try {
            doc = Jsoup.connect(location).get();
            Elements element = doc.select("link[rel~=(?i)^(shortcut|icon|shortcut icon)$]");

            if (element.isEmpty()) {
                return getDefaultBookmarkIcon();
            }

            favIconURLString = element.get(0).attr("abs:href");

        } catch (IOException e) {
            e.printStackTrace();
            return getDefaultBookmarkIcon();
        }

        if (favIconURLString.isEmpty()) {
            System.out.println("Empty");
            return getDefaultBookmarkIcon();
        }

        favIconURLString = LocationUtil.addHostAndProtocol(location, favIconURLString);


        System.out.println("Base URL: " + location + " Decoding: " + favIconURLString + " ext: " + LocationUtil.getExtension(favIconURLString));

        String iconExtension = LocationUtil.getExtension(favIconURLString);

        try {
            if (iconExtension.contains("ico")) {
                final HttpURLConnection connection;
                URL url = new URL(favIconURLString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36");
                List<BufferedImage> decodedIcons = ICODecoder.read(connection.getInputStream());
                if (!decodedIcons.isEmpty()) {
                    return ImageUtil.convertBufferedImageToJavaFXImage(decodedIcons.get(0), 0, 16, true, true);

                }
            } else if (iconExtension.contains("png")) {
                return new Image(favIconURLString, 0, 16, true, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return getDefaultBookmarkIcon();
        }
        return getDefaultBookmarkIcon();
    }
    */

    private static class LazyHolder {
        static final IconManager INSTANCE = new IconManager();
    }

    public static IconManager getInstance() {
        return IconManager.LazyHolder.INSTANCE;
    }


    private IconManager() {
        // private to prevent instancing
    }

}
