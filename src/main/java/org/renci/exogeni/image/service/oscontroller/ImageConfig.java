package org.renci.exogeni.image.service.oscontroller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ImageConfig {
    private Properties properties;
    private boolean load = true;

    private static final String EXO_IMAGE_HOME = "EXO_IMAGE_HOME";
    public static final String osAuthUrl = "os.authUrl";
    public static final String osRegion = "os.region";

    public String getOsAuthUrl() {
        return properties.getProperty(ImageConfig.osAuthUrl);
    }
    public String getOsRegion() { return properties.getProperty(ImageConfig.osRegion); }

    private static final ImageConfig fINSTANCE = new ImageConfig();
    public static ImageConfig getInstance() {
        return fINSTANCE;
    }
    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    private ImageConfig() {
        load();
    }


    public void load() {
        InputStream input = null;

        try {
            if(load) {
                if(getExoImageHome() == null) {
                    input = ImageConfig.class.getClassLoader().getResourceAsStream("application.properties");
                }
                else {
                    String HomeDirectory = ImageConfig.getExoImageHome();
                    String ConfigDirectory = HomeDirectory + System.getProperty("file.separator") + "config"
                            + System.getProperty("file.separator");
                    String ExoImageServiceConfigurationFile = ConfigDirectory + "application.properties";
                    input = new FileInputStream(ExoImageServiceConfigurationFile);
                }
                properties = new Properties();
                // load a properties file
                properties.load(input);
                load = false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getExoImageHome() {
        // first check if EXO_IMAGE_HOME is defined as a system property
        String exoImageHome = System.getProperty(EXO_IMAGE_HOME);
        if (exoImageHome == null){
            // next check if there is an environment variable EXO_IMAGE_HOME
            exoImageHome = System.getenv(EXO_IMAGE_HOME);
        }

        return exoImageHome;
    }
}
