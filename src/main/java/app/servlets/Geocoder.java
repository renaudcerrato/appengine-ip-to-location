package app.servlets;


import com.google.gson.JsonObject;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class Geocoder extends HttpServlet {

  private static final Logger LOGGER = Logger.getLogger(Geocoder.class.getName());

  private static final String X_APP_ENGINE_COUNTRY = "X-AppEngine-Country";
  private static final String X_APP_ENGINE_REGION = "X-AppEngine-Region";
  private static final String X_APP_ENGINE_CITY = "X-AppEngine-City";
  private static final String X_APP_ENGINE_CITY_LAT_LONG = "X-AppEngine-CityLatLong";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    final String country = request.getHeader(X_APP_ENGINE_COUNTRY);
    final String region = request.getHeader(X_APP_ENGINE_REGION);
    final String city = request.getHeader(X_APP_ENGINE_CITY);
    final String latlon = request.getHeader(X_APP_ENGINE_CITY_LAT_LONG);

    JsonObject json = new JsonObject();
    json.addProperty("ip", Utils.getClientAddr(request));
    json.addProperty("country", country);
    json.addProperty("city", city);
    json.addProperty("region", region);


    if(latlon != null) {
      final String[] loc = latlon.split(",");

      if(loc.length == 2) {
        try {
          double lat = Double.valueOf(loc[0]);
          double lon = Double.valueOf(loc[1]);
          json.addProperty("lat", lat);
          json.addProperty("long", lon);
        }catch(NumberFormatException e) {
          LOGGER.warning("unable to parse lat/long: " + latlon);
        }
      }
    }

    response.setContentType("application/json");
    response.getWriter().print(json.toString());
  }
}