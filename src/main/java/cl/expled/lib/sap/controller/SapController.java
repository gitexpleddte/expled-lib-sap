package cl.expled.lib.sap.controller;
/*solo puede ser usado con la configuracion sapjco en el servidor*/
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.XML;


import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SapController {
	private String JCO_ASHOST;
	private String JCO_SYSNR;
	private String JCO_CLIENT;
	private String JCO_USER;
	private String JCO_PASSWD;
	private String JCO_LANG;
	public String CONNECION_ID="SCO";
	public JCoDestination destination;
	
	private Boolean commit_context; 
	
	public SapController() {}
	public SapController setJCO_ASHOST(String s) {JCO_ASHOST=s;return this;}
	public SapController setJCO_SYSNR(String s) {JCO_SYSNR=s;return this;}
	public SapController setJCO_CLIENT(String s) {JCO_CLIENT=s;return this;}
	public SapController setJCO_USER(String s) {JCO_USER=s;return this;}
	public SapController setJCO_PASSWD(String s) {JCO_PASSWD=s;return this;}
	public SapController setJCO_LANG(String s) {JCO_LANG=s;return this;}
	public SapController setCONNECION_ID(String s) {CONNECION_ID=s;return this;}
	public boolean connect()
	{
		try {
			SapDestinationProvider provider = SapDestinationProvider.getInstance();
			Properties connectProperties = new Properties();
			connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, JCO_ASHOST);
			connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, JCO_SYSNR);
			connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, JCO_CLIENT);
			connectProperties.setProperty(DestinationDataProvider.JCO_USER, JCO_USER);
			connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, JCO_PASSWD);
			connectProperties.setProperty(DestinationDataProvider.JCO_LANG, JCO_LANG);
			connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "60000");
			connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "0");
			
			//Random rand = new Random();
			//if(CONNECION_ID==null)CONNECION_ID = this.CONNECION_ID;
			provider.addDestinationByName(CONNECION_ID, connectProperties);
			
			
			if(!com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()) {
				com.sap.conn.jco.ext.Environment.registerDestinationDataProvider( provider );
			}
			destination = JCoDestinationManager.getDestination(CONNECION_ID);
		}catch (Exception ex) {
			System.out.println(ex);
			throw new RuntimeException(ex.getMessage());
		}
		return destination.isValid();
	}
	
	
	/**
	Conectar por medio de un archivio jcoDestination en carpeta tomcat por defecto
	@param DestinationFolder	ubicacion fisica del archivo
	@param ConnectionName	nombre del archivo .jcoDestination
	@return boolean	destination.isValid()
	@throws JCoException
	*/
	public boolean connect(String DestinationFolder,String ConnectionName) throws JCoException
	{
		//JCoDestination destination;
		DestinationFolder=DestinationFolder!=null?DestinationFolder:"";
		Random rand = new Random(); 
		int randConnectionName = rand.nextInt(99999); 
		ConnectionName = ConnectionName!=null?ConnectionName:randConnectionName+"";
		File destCfg;
		try {
			Properties connectProperties = new Properties();
			connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, JCO_ASHOST);
			connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, JCO_SYSNR);
			connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, JCO_CLIENT);
			connectProperties.setProperty(DestinationDataProvider.JCO_USER, JCO_USER);
			connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, JCO_PASSWD);
			connectProperties.setProperty(DestinationDataProvider.JCO_LANG, JCO_LANG);
			connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "60000");
			connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "0");
			
			destCfg = new File(DestinationFolder+"/"+ConnectionName+".jcoDestination");
			System.out.println(destCfg.getAbsolutePath());
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			connectProperties.store(fos, "");
			fos.close();
		}catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unable to create the  destination files: "+ex.getMessage(), ex);
			
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		destination = JCoDestinationManager.getDestination(DestinationFolder+"/"+ConnectionName);
		destCfg.delete();
		return destination.isValid();
	}
	
	/**
	Conectar por medio de un archivio jcoDestination en carpeta tomcat por defecto
	@param DestinationFolder	ubicacion fisica del archivo
	@param ConnectionName	nombre del archivo .jcoDestination
	@param DeleteDestinationfile	verdadero = eliminar archivo .jcoDestination, falso = no eliminar archivo .jcoDestination
	@param ReplaceFile	vardadero = reemplaza el archivo .jcoDestination, falso no reemplazar
	@return boolean destination.isValid()
	@throws JCoException
	*/
	public boolean connect(
			String DestinationFolder,
			String ConnectionName,
			boolean DeleteDestinationFile,
			boolean ReplaceDestinationFile)
		throws JCoException
	{
		//JCoDestination destination;
		DestinationFolder=DestinationFolder!=null?DestinationFolder:"";
		Random rand = new Random(); 
		int randConnectionName = rand.nextInt(99999); 
		ConnectionName = ConnectionName!=null?ConnectionName:randConnectionName+"";
		File destCfg = new File("");
		try {
			if(ReplaceDestinationFile) {
				Properties connectProperties = new Properties();
				connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, JCO_ASHOST);
				connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, JCO_SYSNR);
				connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, JCO_CLIENT);
				connectProperties.setProperty(DestinationDataProvider.JCO_USER, JCO_USER);
				connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, JCO_PASSWD);
				connectProperties.setProperty(DestinationDataProvider.JCO_LANG, JCO_LANG);
				connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "60000");
				connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "0");
				
				destCfg = new File(DestinationFolder+"/"+ConnectionName+".jcoDestination");
				System.out.println(destCfg.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(destCfg, false);
				connectProperties.store(fos, "");
				fos.close();
				
			}
		}catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unable to create the  destination files: "+ex.getMessage(), ex);
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		destination = JCoDestinationManager.getDestination(DestinationFolder+"/"+ConnectionName);
		if(DeleteDestinationFile && destCfg!=null) {
			destCfg.delete();
		}
		return destination.isValid();
	}
	
	public boolean connect(String DestinationFolder,String ConnectionName, Properties connectProperties) throws JCoException
	{
		//JCoDestination destination;
		DestinationFolder=DestinationFolder!=null?DestinationFolder:"";
		Random rand = new Random(); 
		int randConnectionName = rand.nextInt(99999); 
		ConnectionName = ConnectionName!=null?ConnectionName:randConnectionName+"";
		File destCfg;
		try {
			destCfg = new File(DestinationFolder+"/"+ConnectionName+".jcoDestination");
			System.out.println(destCfg.getAbsolutePath());
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			connectProperties.store(fos, "");
			fos.close();
		}catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unable to create the  destination files: "+ex.getMessage(), ex);
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		destination = JCoDestinationManager.getDestination(DestinationFolder+"/"+ConnectionName);
		destCfg.delete();
		return destination.isValid();
	}
	
	/**
	Conectar por medio de un archivio jcoDestination en carpeta tomcat por defecto
	@param DestinationFolder	ubicacion fisica del archivo
	@param ConnectionName	nombre del archivo .jcoDestination
	@param connectProperties	propiedades del archivo .jcoDestination
	@param DeleteDestinationFile	verdadero = eliminar archivo .jcoDestination, falso = no eliminar archivo .jcoDestination
	@param ReplaceDestinationFile	vardadero = reemplaza el archivo .jcoDestination, falso no reemplazar
	@return boolean destination.isValid()
	@throws JCoException
	*/
	public boolean connect(
			String DestinationFolder,
			String ConnectionName, 
			Properties connectProperties,
			boolean DeleteDestinationFile,
			boolean ReplaceDestinationFile
		) throws JCoException
	{
		//JCoDestination destination;
		DestinationFolder=DestinationFolder!=null?DestinationFolder:"";
		Random rand = new Random(); 
		int randConnectionName = rand.nextInt(99999); 
		ConnectionName = ConnectionName!=null?ConnectionName:randConnectionName+"";
		File destCfg = new File("");
		try {
			if(ReplaceDestinationFile) {
				destCfg = new File(DestinationFolder+"/"+ConnectionName+".jcoDestination");
				System.out.println(destCfg.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(destCfg, false);
				connectProperties.store(fos, "");
				fos.close();
			}
			destination = JCoDestinationManager.getDestination(DestinationFolder+"/"+ConnectionName);
			if(DeleteDestinationFile && destCfg!=null) {
				destCfg.delete();
			}
		}catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unable to create the  destination files: "+ex.getMessage(), ex);
		}catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return destination.isValid();
	}
	
	
	public boolean resetDestinationDataProvider() {
		SapDestinationProvider provider = SapDestinationProvider.getInstance();
		if(com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()) {
			com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(provider);
		}
		return true;
	}
	
	public boolean connect(Properties connectProperties)
	{
		try {
			SapDestinationProvider provider = SapDestinationProvider.getInstance();
			//Random rand = new Random();
			//if(CONNECION_ID==null)CONNECION_ID = "";
			provider.addDestinationByName(CONNECION_ID, connectProperties);
			if(!com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()) {
				com.sap.conn.jco.ext.Environment.registerDestinationDataProvider( provider );
			}
			destination = JCoDestinationManager.getDestination(CONNECION_ID);
			
		}catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		return destination.isValid();
	}
	
	public boolean connect(String CONNECION_ID)
	{
		try {
			
			this.CONNECION_ID=CONNECION_ID;
			SapDestinationProvider provider = SapDestinationProvider.getInstance();
			if(!com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()) {
				com.sap.conn.jco.ext.Environment.registerDestinationDataProvider( provider );
			}
			destination = JCoDestinationManager.getDestination(CONNECION_ID);
		}catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		return destination.isValid();
	}
	
	public JSONObject callRfc(JSONObject json) {	
		JSONObject r = new JSONObject();
		JSONObject data= new JSONObject();
		try {
			if(!json.has("RFC")) {
				r.put("error",1);
				r.put("message","RFC no asignado");
				return r;
			}
			if(json.has("COMMIT")&&json.getBoolean("COMMIT")) {
				JCoContext.begin(destination);
				commit_context=true;
			}
			JCoRepository repo = destination.getRepository();
			JCoFunction function = repo.getFunction(json.getString("RFC"));
			//System.out.println(destination);
			//System.out.println(function.toXML().toString());
			if(json.has("getBase") && json.getBoolean("getBase")) {
				data = XML.toJSONObject(function.toXML().toString());
				
				//obtener tables
				if(function.getImportParameterList()  instanceof JCoParameterList) {
					Iterator<JCoField> rec =  function.getImportParameterList().iterator();
					
					JSONObject tableObject= new JSONObject();
					//iterar por cada input de IMPORT
					while(rec.hasNext()) {
						JCoField obj = rec.next();
						String tableName = obj.getName();
						JSONArray tableArray = new JSONArray();
						JSONObject itemTable = new JSONObject();
						if(obj.isTable()) {
							Iterator<JCoField> rec2 = obj.getTable().iterator();
							while(rec2.hasNext()) {
								JCoField fieldItem = rec2.next();
								itemTable.put(fieldItem.getName(), "");
							}
							tableArray.put(itemTable);
							tableObject.put(tableName,tableArray);
						}else if(obj.isStructure()){
							Iterator<JCoField> rec2 = obj.getStructure().iterator();
							while(rec2.hasNext()) {
								JCoField fieldItem = rec2.next();
								itemTable.put(fieldItem.getName(), "");
							}
							tableObject.put(tableName,itemTable);
						}else {
							tableObject.put(tableName, "");
						}
					}
					r.put("INPUT",tableObject);
				}
				if(function.getTableParameterList()  instanceof JCoParameterList) {
					Iterator<JCoField> rec =  function.getTableParameterList().iterator();
					
					JSONObject tableObject= new JSONObject();
					//iterar por cada input de IMPORT
					while(rec.hasNext()) {
						JCoField obj = rec.next();
						String tableName = obj.getName();
						JSONArray tableArray = new JSONArray();
						JSONObject itemTable = new JSONObject();
						
						if(obj.isTable()) {
							Iterator<JCoField> rec2 = obj.getTable().iterator();
							while(rec2.hasNext()) {
								JCoField fieldItem = rec2.next();
								itemTable.put(fieldItem.getName(), "");
							}
							tableArray.put(itemTable);
							tableObject.put(tableName,tableArray);
						}else if(obj.isStructure()){
							Iterator<JCoField> rec2 = obj.getStructure().iterator();
							while(rec2.hasNext()) {
								JCoField fieldItem = rec2.next();
								itemTable.put(fieldItem.getName(), "");
							}
							tableObject.put(tableName,itemTable);
						}else {
							tableObject.put(tableName, "");
						}
						
						//System.out.println("");
						//System.out.println(obj.getName());
					}
					r.put("TABLES",tableObject);
				}
				r.put("RFC",json.getString("RFC"));
				return r;
			}
			//iterar sobre items
			if(json.has("INPUT")) {
				JSONObject input = json.getJSONObject("INPUT");
				Iterator<String> keys = input.keys();
				while(keys.hasNext()) {
				    String key = keys.next();
				    if (input.get(key) instanceof JSONObject) {
				    	JCoStructure inputStructure = function.getImportParameterList().getStructure(key);
			    		JSONObject objArr = input.getJSONObject(key);
			    		Iterator<String> iobj= objArr.keys();
			    		while(iobj.hasNext()) {
					    	String itemKey = iobj.next();
					    	inputStructure.setValue(itemKey, objArr.get(itemKey)+"");
					    }
				    	function.getImportParameterList().setValue(key, inputStructure);
				    }else if(input.get(key) instanceof JSONArray) {
				    	JCoTable inputTable = function.getImportParameterList().getTable(key);
				    	JSONArray arr = input.getJSONArray(key);
				    	Iterator<Object> iArr= arr.iterator();
				    	while(iArr.hasNext()) {
				    		JSONObject objArr = (JSONObject)iArr.next();
				    		Iterator<String> iobj= objArr.keys();
				    		inputTable.appendRow();
				    		while(iobj.hasNext()) {
						    	String itemKey = iobj.next();
						    	
						    	//*Array dentro de arrays*//
						    	if(objArr.get(itemKey) instanceof JSONArray) {
							    	JCoTable inputTableNested = inputTable.getTable(itemKey);
							    	JSONArray arrNested = objArr.getJSONArray(itemKey);
							    	Iterator<Object> iArrNested= arrNested.iterator();
							    	while(iArrNested.hasNext()) {
							    		JSONObject objArrNested = (JSONObject)iArrNested.next();
							    		Iterator<String> iobjNested= objArrNested.keys();
							    		inputTableNested.appendRow();
							    		while(iobjNested.hasNext()) {
									    	String itemKeyNested = iobjNested.next();
									    	inputTableNested.setValue(itemKeyNested, objArrNested.get(itemKeyNested)+"");
									    }
							    	}
							    	inputTable.setValue(itemKey, inputTableNested);
							    }else {
							    	inputTable.setValue(itemKey, objArr.get(itemKey)+"");
							    }
						    	//inputTable.setValue(itemKey, objArr.get(itemKey)+"");
						    	
						    }
				    	}
				    	function.getImportParameterList().setValue(key, inputTable);
				    }else if(input.get(key) instanceof String) {
				    	function.getImportParameterList().setValue(key, input.getString(key));
				    }else if(input.get(key) instanceof Number) {
				    	function.getImportParameterList().setValue(key, input.getString(key));
				    }
				}
			}
			if(json.has("TABLES")) {
				JSONObject input = json.getJSONObject("TABLES");
				Iterator<String> keys = input.keys();
				while(keys.hasNext()) {
				    String key = keys.next();
				    if (input.get(key) instanceof JSONObject) {
				    	JCoStructure inputStructure = function.getTableParameterList().getStructure(key);
			    		JSONObject objArr = input.getJSONObject(key);
			    		Iterator<String> iobj= objArr.keys();
			    		while(iobj.hasNext()) {
					    	String itemKey = iobj.next();
					    	inputStructure.setValue(itemKey, objArr.get(itemKey)+"");
					    }
				    	function.getTableParameterList().setValue(key, inputStructure);
				    }else if(input.get(key) instanceof JSONArray) {
				    	JCoTable inputTable = function.getTableParameterList().getTable(key);
				    	JSONArray arr = input.getJSONArray(key);
				    	Iterator<Object> iArr= arr.iterator();
				    	while(iArr.hasNext()) {
				    		JSONObject objArr = (JSONObject)iArr.next();
				    		Iterator<String> iobj= objArr.keys();
				    		inputTable.appendRow();
				    		while(iobj.hasNext()) {
						    	String itemKey = iobj.next();
						    	//*Array dentro de arrays*//
						    	if(objArr.get(itemKey) instanceof JSONArray) {
							    	JCoTable inputTableNested = inputTable.getTable(itemKey);
							    	JSONArray arrNested = objArr.getJSONArray(itemKey);
							    	Iterator<Object> iArrNested= arrNested.iterator();
							    	while(iArrNested.hasNext()) {
							    		JSONObject objArrNested = (JSONObject)iArrNested.next();
							    		Iterator<String> iobjNested= objArrNested.keys();
							    		inputTableNested.appendRow();
							    		while(iobjNested.hasNext()) {
									    	String itemKeyNested = iobjNested.next();
									    	inputTableNested.setValue(itemKeyNested, objArrNested.get(itemKeyNested)+"");
									    }
							    	}
							    	inputTable.setValue(itemKey, inputTableNested);
							    }else {
							    	inputTable.setValue(itemKey, objArr.get(itemKey)+"");
							    }
						    	//inputTable.setValue(itemKey, objArr.get(itemKey)+"");
						    }
				    	}
				    	function.getTableParameterList().setValue(key, inputTable);
				    }else if(input.get(key) instanceof String) {
				    	function.getTableParameterList().setValue(key, input.getString(key));
				    }else if(input.get(key) instanceof Number) {
				    	function.getTableParameterList().setValue(key, input.getString(key));
				    }
				}
			}
			function.execute(destination);
			if(json.has("COMMIT")&&json.getBoolean("COMMIT")) {
				System.out.println("commit");
				JCoFunction functionC = repo. getFunction("BAPI_TRANSACTION_COMMIT");
				functionC.getImportParameterList().setValue("WAIT", "X");
				functionC.execute(destination);
				JSONObject ocommit = XML.toJSONObject(functionC.toXML().toString());
				r.put("commit",ocommit);
				JCoContext.end(destination);
			}
			
			System.out.println("reponse");
			data = XML.toJSONObject((function.toXML().toString()));
			//System.out.println(XML.toJSONObject(XML.escape(function.toXML().toString())));
			r.put("data",data);
			r.put("error",0);
			r.put("message","OK");
		}catch(Exception ex) {
			ex.printStackTrace();
			r.put("error",1);
			r.put("message",ex.getMessage());
			r.put("e", "Exception call RFC");
			if(commit_context) {
				try {
					JCoContext.end(destination);
				} catch (JCoException e) {
					e.printStackTrace();
					r.put("error",1);
					r.put("message",e.getMessage());
					r.put("e", "Exception cierre de contexto");
				}
			}
			
		}
		return r;
	}
	public static String getDefaultCharEncoding(){
        byte [] bArray = {'w'};
        InputStream is = new ByteArrayInputStream(bArray);
        InputStreamReader reader = new InputStreamReader(is);
        String defaultCharacterEncoding = reader.getEncoding();
        return defaultCharacterEncoding;
    }

}