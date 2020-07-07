package cl.expled.lib.sap.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SapDestinationProvider implements DestinationDataProvider{
	
	private Map<String, Properties> destinationMap = new HashMap();
	private static final Logger LOG = Logger.getLogger(SapDestinationProvider.class.getName());
	private static SapDestinationProvider instance = null;
	public static SapDestinationProvider getInstance() {

		if (instance == null) {
			instance = new SapDestinationProvider();
		}
		return instance;

	}

	public SapDestinationProvider() {

		if (instance == null) {
			instance = this;
		}
	}

	public Boolean addDestinationByName(String destName, Properties destProperties) {

		if (destinationMap.containsKey(destName)) {
			LOG.log(Level.WARNING, "Unable to load destination " + destName + ", already loaded.");
			return false;
	
		} else {
			destinationMap.put(destName, destProperties);
			LOG.log(Level.INFO, "Destination " + destName + " properties added/updated.");
			return true;
		}
	}
	
	public Boolean addDestinationByName(String destName, Properties destProperties,boolean replaceIfExist) {

		if (!replaceIfExist && destinationMap.containsKey(destName)) {
			LOG.log(Level.WARNING, "Unable to load destination " + destName + ", already loaded.");
			return false;
		}else if (replaceIfExist && destinationMap.containsKey(destName)) {
			destinationMap.put(destName, destProperties);
			LOG.log(Level.INFO, "Destination " + destName + " properties updated.");
			return true;
		} else {
			destinationMap.put(destName, destProperties);
			LOG.log(Level.INFO, "Destination " + destName + " properties added.");
			return true;
		}
	}
	
	public void removeDestination(final String destinationName) {
		if(destinationMap.containsKey(destinationName)) {
	        destinationMap.remove(destinationName);
		}
    }

	public Map<String, Properties> getDestinations() {
		return destinationMap;
	}

	@Override
	public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {

		// nothing to do

	}

	@Override
	public boolean supportsEvents() {
		return false;
	}

	@Override

	public Properties getDestinationProperties(String destination) {

		Properties props = new Properties();
	
		if (destinationMap.containsKey(destination)) {
			props = destinationMap.get(destination);
			return props;
	
		} else {
			LOG.log(Level.WARNING, "Destination "+destination+" properties not found.");
			return null;
		}

	}
}