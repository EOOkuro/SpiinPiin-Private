package com.gotanyalo.spiinpiin.core.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CountyTest {
	
	private final String baseUrl = "http://localhost:8080/spin/api/base/";
	
	private JSONParser parser;
	
	private Client client;

	/*@Deployment
	public static JavaArchive createDeployment(){
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(Country.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}*/
	
	@Before
	public void setUp() throws Exception {
		this.client = ClientBuilder.newClient();
		this.parser = new JSONParser();
	}
		
	@Test
	public void verifyCountyCount() {
		try {
			WebTarget target = this.client.target(this.baseUrl + "lcounty/KEN");
			Response respose = target.request().get();
			if (respose.getStatus() != 200){
				throw new Exception("Operation failed! " + respose.getStatus());
			}
			
			String rst = respose.readEntity(String.class);
						
			JSONArray obj = (JSONArray)parser.parse(rst);
			
			assertTrue(obj.size() + " == 47 Kenyan counties found!", obj.size() == 47);			
		} catch (Exception e) {
			assertTrue(this.getErrorMessageStack(e), false);
		}		
	}
	
	@Test
	public void verifySubCountyCount() {
		try {
			WebTarget target = this.client.target(this.baseUrl + "lcounty/KEN");
			Response respose = target.request().get();
			if (respose.getStatus() != 200){
				throw new Exception("Operation failed! " + respose.getStatus());
			}
			
			String rst = respose.readEntity(String.class);
						
			JSONArray obj = (JSONArray)parser.parse(rst);
			
			for (int i = 0; i < obj.size(); i++) {		
				String county = ((JSONObject)obj.get(i)).get("name").toString();
				target = this.client.target(this.baseUrl + "lscounty/" + ((JSONObject)obj.get(i)).get("id").toString());
				respose = target.request().get();
				if (respose.getStatus() != 200){
					throw new Exception("Subcounty-Operation failed! " + respose.getStatus());
				}
				
				rst = respose.readEntity(String.class);
							
				JSONArray xobj = (JSONArray)parser.parse(rst);
				
				int rstx = this.verifySubcountyNo(county, xobj.size());
				assertTrue(rstx + " == " + xobj.size() + " - " + county + " Kenyan counties found!", xobj.size() == rstx);
			}			
		} catch (Exception e) {
			assertTrue(this.getErrorMessageStack(e), false);
		}		
	}
	
	private int verifySubcountyNo(String county, int sub){
		if (county == null || county.trim().equalsIgnoreCase("")){
			return -1;
		}
		
		String upperstr = county.toUpperCase();
		
		if (upperstr.equals("BARINGO")){
			return 6;
		} else if (upperstr.equals("BOMET")){
			return 4;
		} else if (upperstr.equals("BUNGOMA")){
			return 9;
		} else if (upperstr.equals("BUSIA")){
			return 7;
		} else if (upperstr.equals("ELGEYO MARAKWET")){
			return 4;
		} else if (upperstr.equals("EMBU")){
			return 5;
		} else if (upperstr.equals("GARISSA")){
			return 7;
		} else if (upperstr.equals("HOMABAY")){
			return 6;
		} else if (upperstr.equals("ISIOLO")){
			return 3;
		} else if (upperstr.equals("KAJIADO")){
			return 5;
		} else if (upperstr.equals("KAKAMEGA")){
			return 12;
		} else if (upperstr.equals("KERICHO")){
			return 5;
		} else if (upperstr.equals("KIAMBU")){
			return 10;
		} else if (upperstr.equals("KILIFI")){
			return 6;
		} else if (upperstr.equals("KIRINYAGA")){
			return 5;
		} else if (upperstr.equals("KISII")){
			return 9;
		} else if (upperstr.equals("KISUMU")){
			return 7;
		} else if (upperstr.equals("KITUI")){
			return 16;
		} else if (upperstr.equals("KWALE")){
			return 3;
		} else if (upperstr.equals("LAIKIPIA")){
			return 5;
		} else if (upperstr.equals("LAMU")){
			return 2;
		} else if (upperstr.equals("MACHAKOS")){
			return 8;
		} else if (upperstr.equals("MAKUENI")){
			return 9;
		} else if (upperstr.equals("MANDERA")){
			return 6;
		} else if (upperstr.equals("MARSABIT")){
			return 7;
		} else if (upperstr.equals("MERU")){
			return 8;
		} else if (upperstr.equals("MIGORI")){
			return 7;
		} else if (upperstr.equals("MOMBASA")){
			return 4;
		} else if (upperstr.equals("MURANGA")){
			return 8;
		} else if (upperstr.equals("NAIROBI")){
			return 9;
		} else if (upperstr.equals("NAKURU")){
			return 9;
		} else if (upperstr.equals("NANDI")){
			return 5;
		} else if (upperstr.equals("NAROK")){
			return 4;
		} else if (upperstr.equals("NYAMIRA")){
			return 5;
		} else if (upperstr.equals("NYANDARUA")){
			return 7;
		} else if (upperstr.equals("NYERI")){
			return 8;
		} else if (upperstr.equals("SAMBURU")){
			return 3;
		} else if (upperstr.equals("SIAYA")){
			return 6;
		} else if (upperstr.equals("TAITA TAVETA")){
			return 4;
		} else if (upperstr.equals("TANA RIVER")){
			return 3;
		} else if (upperstr.equals("THARAKA NITHI")){
			return 4;
		} else if (upperstr.equals("TRANS NZOIA")){
			return 3;
		} else if (upperstr.equals("TURKANA")){
			return 6;
		} else if (upperstr.equals("UASIN GISHU")){
			return 6;
		} else if (upperstr.equals("VIHIGA")){
			return 5;
		} else if (upperstr.equals("WAJIR")){
			return 8;
		} else {
			// WEST POKOT
			return 4;
		}		
	}
	
	private String getErrorMessageStack(Throwable e){
		if (e == null){
			return "";
		}
		
		StringWriter writer = new StringWriter();
		PrintWriter print = new PrintWriter(writer);		
		e.printStackTrace(print);
		
		return e.getMessage() + " >> " + writer.toString();
	}

}