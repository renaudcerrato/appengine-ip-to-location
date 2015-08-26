/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package ip2location.appengine;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.servlet.http.HttpServletRequest;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "api",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "appengine.ip2location",
                ownerName = "appengine.ip2location",
                packagePath = ""
        )
)
public class Endpoint {

    static final String X_APP_ENGINE_COUNTRY = "X-AppEngine-Country";
    static final String X_APP_ENGINE_REGION = "X-AppEngine-Region";
    static final String X_APP_ENGINE_CITY = "X-AppEngine-City";
    static final String X_APP_ENGINE_CITY_LAT_LONG = "X-AppEngine-CityLatLong";

    /**
     * A simple endpoint method that returns location headers
     */
    @ApiMethod(name="get")
    public IPLocation get(HttpServletRequest req) {
        IPLocation response = new IPLocation(req.getRemoteAddr());


        response.setCountry(req.getHeader(X_APP_ENGINE_COUNTRY));
        response.setRegion(req.getHeader(X_APP_ENGINE_REGION));
        response.setCity(req.getHeader(X_APP_ENGINE_CITY));

        String latLon = req.getHeader(X_APP_ENGINE_CITY_LAT_LONG);

        if(latLon != null) {
            String[] loc = latLon.split(",");
            if(loc.length == 2) {
                double lat = Double.valueOf(loc[0]);
                double lon = Double.valueOf(loc[1]);
                response.setLocation(lat, lon);
            }
        }

        return response;
    }
}
