package com.sedin;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;

public class rajreport {
	 static IDfSession session = null;
	 public static void main(String[] args) throws Exception {
	session =  getSession("ecmops", "Rev@mp#ECM26", "EDMS");
//	generateReport(session,"HO-DMFI","01-01-2026","03-31-2026","nb_letters_ho_dmfi_cgm");
	Aregion(session, "HRMD", "04-01-2025","06-30-2026");
	
	if (session != null) {
		session.getSessionManager().release(session);
		System.out.println("----------------------Session Released----------------------");
	}

}


static IDfSession getSession(String Username, String Password, String Docbase) throws DfException {
    IDfClient client = DfClient.getLocalClient();
    DfLoginInfo dfLoginInfo = new DfLoginInfo();
    dfLoginInfo.setUser(Username);
    dfLoginInfo.setPassword(Password);
    IDfSession session = client.newSession(Docbase, (IDfLoginInfo)dfLoginInfo);
    System.out.println("---------Session Created----------------------");
    return session;
  }
public static void generateReport(IDfSession session,String region,String from_date,String to_date, String login_cgm_group) throws DfException {
	
	System.out.println("Status of reply in Hindi to the letters received in Hindi");
	DfQuery dfQuery = new DfQuery();
	DfQuery dfQuery1 = new DfQuery();
	DfQuery dfQuery2= new DfQuery();
    String dql = "select region, count(r_object_id) as r_object_id from cms_digidak_folder where group_letter_id=false and region = '"+ region +"' and\r\n"
    		+ " languages in ('Hindi', 'Bilingual') and login_cgm_group = '" + login_cgm_group + "' and r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE(' "+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false group by region;";
    
    String dql2 = "select count(r_object_id) as object_id from cms_digidak_folder where any response_to_ioms_id in (select uid_number from cms_digidak_folder where languages in ('Hindi', 'Bilingual')  and r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE(' "+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved'and login_cgm_group = '" + login_cgm_group + "') and languages in ('Hindi', 'Bilingual') \r\n"
    		+ "and r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE(' "+ to_date +"','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false;";
    String dql3 = "select count(r_object_id) as r_object_id from cms_digidak_folder where any response_to_ioms_id in (select uid_number from cms_digidak_folder where languages in ('Hindi', 'Bilingual') and r_creation_date>=DATE(' " + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE(' "+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved'and login_cgm_group = '" + login_cgm_group + "') and languages = 'English' and r_creation_date>=DATE(' "+ from_date +" 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE(' "+ to_date +"','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false;";
    
    
    dfQuery.setDQL(dql);
    		dfQuery1.setDQL(dql2);
    		dfQuery2.setDQL(dql3);
    	    
    System.out.println(dql3);
    IDfCollection collection = dfQuery.execute(session, IDfQuery.DF_READ_QUERY);
    System.out.println(1);
    IDfCollection collection1 = dfQuery1.execute(session, IDfQuery.DF_READ_QUERY);
    System.out.println(2);
    IDfCollection collection2 = dfQuery2.execute(session, IDfQuery.DF_READ_QUERY);
    System.out.println(3);
    
    try {
        while (collection.next()) {
        	int attributeValue1  = collection.getInt("r_object_id");
            System.out.println("Total no. of letters received in Hindi:" + attributeValue1);
            
    while (collection1.next()) {
    	int attributeValue2 = collection1.getInt("object_id");
        System.out.println("Out of the above how many were replied to in Hindi:" + attributeValue2);
        
    while (collection2.next()) {
    	int attributeValue3  = collection2.getInt("r_object_id");
        System.out.println("Out of the above how many were replied to in English:" + attributeValue3);
        int total =  attributeValue1-attributeValue2-attributeValue3;
        System.out.println("total :" +total);
    }
} 
        }
    }finally {
    collection2.close();
    collection2.close();
    collection2.close();
   
}
rajreport.Aregion(session, region, from_date, to_date,login_cgm_group);
}

public static void Aregion (IDfSession session,String region,String from_date,String to_date, String login_cgm_group) throws DfException {
	DfQuery dfQuery = new DfQuery();
	DfQuery dfQuery1 = new DfQuery();
	DfQuery dfQuery2= new DfQuery();
	
	System.out.println("Status of letters received in English & replied to in Hindi (applicable to offices located in ‘A’ & ‘B regions)");
	System.out.println("A region");
	
    String dql = "select region, count(r_object_id) as r_object_id from cms_digidak_folder where \r\n"
    		+ "region='"+ region +"' and login_region in ('Uttarakhand','Uttar Pradesh', 'Rajasthan', 'New Delhi', 'Himachal Pradesh', 'Madhya Pradesh', 'Jharkhand',\r\n"
    		+ "'Haryana', 'Chhattisgarh', 'Bihar', 'Andaman and Nicobar','Bird Lucknow','NBSC Lucknow') and languages = 'English' and \r\n"
    		+ " status!='Saved' and login_cgm_group = '" + login_cgm_group + "' and \r\n"
    		+ "r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')and is_migrated = false group by region;";
    
    String dql2 = "select count(r_object_id) as object_id from cms_digidak_folder where any response_to_ioms_id in(select uid_number from cms_digidak_folder where \r\n"
    		+ "region='"+ region +"' and login_region in ('Uttarakhand','Uttar Pradesh', 'Rajasthan', 'New Delhi', 'Himachal Pradesh', 'Madhya Pradesh', 'Jharkhand',\r\n"
    		+ "'Haryana', 'Chhattisgarh','Bihar', 'Andaman and Nicobar','Bird Lucknow','NBSC Lucknow') and \r\n"
    		+ "languages = 'English'\r\n"
    		+ " and  status!='Saved' and r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss') and login_cgm_group = '" + login_cgm_group + "') and languages in ('Hindi', 'Bilingual') and \r\n"
    		+ "r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false;";
    String dql3 = "select count(r_object_id) as r_object_id from cms_digidak_folder where any response_to_ioms_id in(select uid_number from cms_digidak_folder where \r\n"
    		+ "region='"+ region +"' and login_region in ('Uttarakhand','Uttar Pradesh', 'Rajasthan', 'New Delhi', 'Himachal Pradesh', 'Madhya Pradesh', 'Jharkhand',\r\n"
    		+ "'Haryana', 'Chhattisgarh','Bihar', 'Andaman and Nicobar','Bird Lucknow','NBSC Lucknow') and \r\n"
    		+ "languages = 'English' and  status!='Saved' and r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')and login_cgm_group = '" + login_cgm_group + "') and languages = 'English' and \r\n"
    		+ "r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false;";
    
    
    dfQuery.setDQL(dql);
    		dfQuery1.setDQL(dql2);
    		dfQuery2.setDQL(dql3);
    	    
   System.out.println(dql);
    IDfCollection collection = dfQuery.execute(session, IDfQuery.DF_READ_QUERY);
    IDfCollection collection1 = dfQuery1.execute(session, IDfQuery.DF_READ_QUERY);
    IDfCollection collection2 = dfQuery2.execute(session, IDfQuery.DF_READ_QUERY);
    
    try {
        while (collection.next()) {
        	int attributeValue1  = collection.getInt("r_object_id");
        	System.out.println("Total no. of letters received in English:" + attributeValue1);
        	
      
    while (collection1.next()) {
    	int attributeValue2 = collection1.getInt("object_id");
        System.out.println("Out of the above how many were replied to in Hindi:" + attributeValue2);
    

    while (collection2.next()) {
    	int attributeValue3  = collection2.getInt("r_object_id");
        System.out.println("Out of the above how many were replied to in English:" + attributeValue3);
        int total =  attributeValue1-attributeValue2-attributeValue3;
        System.out.println("total :" +total);
        rajreport.Bregion(session, region, from_date, to_date, login_cgm_group);
    }
} 
        }
    }finally {
    collection2.close();
    collection2.close();
    collection2.close();
   
}
}

public static void Bregion(IDfSession session,String region,String from_date,String to_date, String login_cgm_group) throws DfException {
	DfQuery dfQuery = new DfQuery();
	DfQuery dfQuery1 = new DfQuery();
	DfQuery dfQuery2= new DfQuery();
	
	System.out.println("B region1	");
	
    String dql = "select count(r_object_id) as r_object_id from cms_digidak_folder where \r\n"
    		+ "region='"+ region +"' and login_region in ('Punjab','Maharashtra', 'Gujarat', 'DIT', 'DOR', 'SECY', 'RMD',\r\n"
    				+ "'SPD', 'AD', 'RAJ', 'SPPID', 'FD', 'FSDD', 'HRMD', 'ID', 'IDD', 'LAW', 'GSD', 'RMSMED', 'DCAS', 'DDMABI', 'DEAR', 'DMFI',\r\n"
    				+ "'DOS', 'DPSP', 'DSM', 'DSSI', 'CCD', 'CPD', 'CVC', 'CHMNS', 'DMDS1', 'DMDS2','CSDD','CISO','PFD','SDCC','DDSI') and languages = 'English' and \r\n"
    		+ " status!='Saved' and login_cgm_group = '" + login_cgm_group + "' and \r\n"
    		+ "r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')and is_migrated = false;";
    
    String dql2 = "select count(r_object_id) as object_id from cms_digidak_folder where any response_to_ioms_id in(select uid_number from cms_digidak_folder where \r\n"
    		+ "region='"+ region +"' and login_region in ('Punjab','Maharashtra', 'Gujarat', 'DIT', 'DOR', 'SECY', 'RMD',\r\n"
    				+ "'SPD', 'AD', 'RAJ', 'SPPID', 'FD', 'FSDD', 'HRMD', 'ID', 'IDD', 'LAW', 'GSD', 'RMSMED', 'DCAS', 'DDMABI', 'DEAR', 'DMFI',\r\n"
    				+ "'DOS', 'DPSP', 'DSM', 'DSSI', 'CCD', 'CPD', 'CVC', 'CHMNS', 'DMDS1', 'DMDS2','CSDD','CISO','PFD','SDCC','DDSI') and \r\n"
    		+ "languages = 'English'\r\n"
    		+ " and  status!='Saved' and r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')and login_cgm_group = '" + login_cgm_group + "') and languages in ('Hindi', 'Bilingual') and \r\n"
    		+ "r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false;";
    String dql3 = "select count(r_object_id) as r_object_id from cms_digidak_folder where any response_to_ioms_id in(select uid_number	 from cms_digidak_folder where \r\n"
    		+ "region='"+ region +"' and login_region in ('Punjab','Maharashtra', 'Gujarat', 'DIT', 'DOR', 'SECY', 'RMD',\r\n"
    				+ "'SPD', 'AD', 'RAJ', 'SPPID', 'FD', 'FSDD', 'HRMD', 'ID', 'IDD', 'LAW', 'GSD', 'RMSMED', 'DCAS', 'DDMABI', 'DEAR', 'DMFI',\r\n"
    				+ "'DOS', 'DPSP', 'DSM', 'DSSI', 'CCD', 'CPD', 'CVC', 'CHMNS', 'DMDS1', 'DMDS2','CSDD','CISO','PFD','SDCC') and \r\n"
    		+ "languages = 'English' and  status!='Saved' and r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and \r\n"
    		+ "r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')and login_cgm_group = '" + login_cgm_group + "') and languages = 'English' and \r\n"
    		+ "r_creation_date>=DATE('" + from_date + " 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+ to_date +" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false;";
    
    
    dfQuery.setDQL(dql);
    		dfQuery1.setDQL(dql2);
    		dfQuery2.setDQL(dql3);
    	    
    		System.out.println(dql);
    IDfCollection collection = dfQuery.execute(session, IDfQuery.DF_READ_QUERY);
    IDfCollection collection1 = dfQuery1.execute(session, IDfQuery.DF_READ_QUERY);
    IDfCollection collection2 = dfQuery2.execute(session, IDfQuery.DF_READ_QUERY);
    
    try {
        while (collection.next()) {
        	int attributeValue1  = collection.getInt("r_object_id");
        	System.out.println("Total no. of letters received in English:" + attributeValue1);
       
    while (collection1.next()) {
    	int attributeValue2 = collection1.getInt("object_id");
        System.out.println("Out of the above how many were replied to in Hindi:" + attributeValue2);
   
    while (collection2.next()) {
    	int attributeValue3  = collection2.getInt("r_object_id");
        System.out.println("Out of the above how many were replied to in English:" + attributeValue3);
        int total =  attributeValue1-attributeValue2-attributeValue3;
        System.out.println("total :" +total);
    }
} 
        }
    }finally {
    collection2.close();
    collection2.close();
    collection2.close();
   
}
}

public static void Aregion (IDfSession session,String login_region,String from_date,String to_date) throws DfException {
	DfQuery dfQuery = new DfQuery();
	DfQuery dfQuery1 = new DfQuery();
	
	System.out.println("Details of original letters (including Emails) issued");
	System.out.println("A region");
	
    String dql = "select count(r_object_id) as r_object_id from cms_digidak_folder where \r\n"
    		+ "login_region='"+login_region+"' and languages in ('Hindi', 'Bilingual')  and region in ('RO-UK',\r\n"
    		+ "'RO-UP',\r\n"
    		+ "'RO-RJ',\r\n"
    		+ "'RO-DL',\r\n"
    		+ "'RO-HP',\r\n"
    		+ "'RO-MP',\r\n"
    		+ "'RO-JH',\r\n"
    		+ "'RO-HR',\r\n"
    		+ "'RO-CH',\r\n"
    		+ "'RO-BR', \r\n"
    		+ "'RO-AN',\r\n"
    		+ "'TE-BL',\r\n"
    		+ "'TE-NC') and decision='Outward'  and status!='Saved' and \r\n"
    		+ "r_creation_date>=DATE('"+from_date+" 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+to_date+" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false;";
    
    String dql2 = "select count(r_object_id) as object_id from cms_digidak_folder where \r\n"
    		+ "login_region='"+login_region+"' and languages in ('English')  and region in ('RO-UK',\r\n"
    		+ "'RO-UP',\r\n"
    		+ "'RO-RJ',\r\n"
    		+ "'RO-DL',\r\n"
    		+ "'RO-HP',\r\n"
    		+ "'RO-MP',\r\n"
    		+ "'RO-JH',\r\n"
    		+ "'RO-HR',\r\n"
    		+ "'RO-CH',\r\n"
    		+ "'RO-BR', \r\n"
    		+ "'RO-AN',\r\n"
    		+ "'TE-BL',\r\n"
    		+ "'TE-NC') and decision='Outward'  and status!='Saved' and \r\n"
    		+ "r_creation_date>=DATE('"+from_date+" 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+to_date+" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
    		+ "and status != 'Saved' and is_migrated = false";
 
    
    
    dfQuery.setDQL(dql);
    		dfQuery1.setDQL(dql2);
    	    
    System.out.println(dql2);
    IDfCollection collection = dfQuery.execute(session, IDfQuery.DF_READ_QUERY);
    IDfCollection collection1 = dfQuery1.execute(session, IDfQuery.DF_READ_QUERY);
    
    try {
        while (collection.next()) {
        	int attributeValue  = collection.getInt("r_object_id");
            System.out.println("Hindi/Bilingual :" + attributeValue);
        while (collection1.next()) {
        	int attributeValue1  = collection1.getInt("object_id");
            System.out.println("English :" + attributeValue1);
            int Total = (attributeValue) + (attributeValue1);
            System.out.println("Total : " +Total);
            Double percentage = (double) attributeValue/Total*100;
            System.out.println("percentage :  " + percentage);
          rajreport.Bregion(session, login_region, from_date, to_date);
        }
    }
        
    }
    
    
    finally {
    	
    	collection.close();
        collection1.close();
       
    }
}
    public static void Bregion (IDfSession session,String login_region,String from_date,String to_date) throws DfException {
    	DfQuery dfQuery = new DfQuery();
    	DfQuery dfQuery1 = new DfQuery();
    	
    	
    	System.out.println("B region");
    	
        String dql = "select count(r_object_id) as r_object_id from cms_digidak_folder where \r\n"
        		+ "login_region='"+login_region+"' and languages in ('Hindi', 'Bilingual') and region in ('RO-PN','RO-MH', 'RO-GJ', 'HO-PFD','HO-DIT',\r\n"
        		+ "'HO-DOR',\r\n"
        		+ "'HO-SECY',\r\n"
        		+ "'HO-RMD',\r\n"
        		+ "'HO-SPD',\r\n"
        		+ "'HO-AD',\r\n"
        		+ "'HO-RAJ',\r\n"
        		+ "'HO-SPPID',\r\n"
        		+ "'HO-FD',\r\n"
        		+ "'HO-FSDD',\r\n"
        		+ "'HO-FSPD',\r\n"
        		+ "'HO-HRMD',\r\n"
        		+ "'HO-ID',\r\n"
        		+ "'HO-IDD',\r\n"
        		+ "'HO-LAW',\r\n"
        		+ "'HO-GSD',\r\n"
        		+ "'HO-RMSMED',\r\n"
        		+ "'HO-DCAS',\r\n"
        		+ "'HO-DDMABI',\r\n"
        		+ "'HO-DEAR',\r\n"
        		+ "'HO-DMFI',\r\n"
        		+ "'HO-DOS',\r\n"
        		+ "'HO-DPSP',\r\n"
        		+ "'HO-DSM',\r\n"
        		+ "'HO-DSSI',\r\n"
        		+ "'HO-CC',\r\n"
        		+ "'HO-CCD',\r\n"
        		+ "'HO-CPD',\r\n"
        		+ "'HO-CVC',\r\n"
        		+ "'HO-CHMNS',\r\n"
        		+ "'HO-DMDS1',\r\n"
        		+ "'HO-DMDS2',\r\n"
        		+ "'HO-CISO',\r\n"
        		+ "'HO-DDSI',\r\n"
        		+ "'HO-CSDD','HO-DOS') and decision='Outward'  and status!='Saved' and \r\n"
        		+ "r_creation_date>=DATE('"+from_date+" 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+to_date+" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
        		+ "and status != 'Saved' and is_migrated = false;";
        
        String dql2 = "select count(r_object_id) as object_id from cms_digidak_folder where \r\n"
        		+ "login_region='"+login_region+"' and languages in ('English') and region in ('RO-PN','RO-MH', 'RO-GJ', 'HO-PFD','HO-DIT',\r\n"
        		+ "'HO-DOR',\r\n"
        		+ "'HO-SECY',\r\n"
        		+ "'HO-RMD',\r\n"
        		+ "'HO-SPD',\r\n"
        		+ "'HO-AD',\r\n"
        		+ "'HO-RAJ',\r\n"
        		+ "'HO-SPPID',\r\n"
        		+ "'HO-FD',\r\n"
        		+ "'HO-FSDD',\r\n"
        		+ "'HO-FSPD',\r\n"
        		+ "'HO-HRMD',\r\n"
        		+ "'HO-ID',\r\n"
        		+ "'HO-IDD',\r\n"
        		+ "'HO-LAW',\r\n"
        		+ "'HO-GSD',\r\n"
        		+ "'HO-RMSMED',\r\n"
        		+ "'HO-DCAS',\r\n"
        		+ "'HO-DDMABI',\r\n"
        		+ "'HO-DEAR',\r\n"
        		+ "'HO-DMFI',\r\n"
        		+ "'HO-DOS',\r\n"
        		+ "'HO-DPSP',\r\n"
        		+ "'HO-DSM',\r\n"
        		+ "'HO-DSSI',\r\n"
        		+ "'HO-CC',\r\n"
        		+ "'HO-CCD',\r\n"
        		+ "'HO-CPD',\r\n"
        		+ "'HO-CVC',\r\n"
        		+ "'HO-CHMNS',\r\n"
        		+ "'HO-DMDS1',\r\n"
        		+ "'HO-DMDS2',\r\n"
        		+ "'HO-CISO',\r\n"
        		+ "'HO-DDSI',\r\n"
        		+ "'HO-CSDD','HO-DOS')and decision='Outward'  and status!='Saved' and \r\n"
        		+ "r_creation_date>=DATE('"+from_date+" 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+to_date+" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
        		+ "and status != 'Saved' and is_migrated = false;";
     
        
        
        dfQuery.setDQL(dql);
        		dfQuery1.setDQL(dql2);
        	    
      System.out.println(dql);
    // System.out.println(dql2);
        IDfCollection collection = dfQuery.execute(session, IDfQuery.DF_READ_QUERY);
        IDfCollection collection1 = dfQuery1.execute(session, IDfQuery.DF_READ_QUERY);
        
        try {
            while (collection.next()) {
            	int attributeValue  = collection.getInt("r_object_id");
                System.out.println("Hindi/Bilingual :" + attributeValue);
            while (collection1.next()) {
            	int attributeValue1  = collection1.getInt("object_id");
                System.out.println("English :" + attributeValue1);
                int Total = (attributeValue) + (attributeValue1);
                System.out.println("Total : " +Total);
                Double percentage = (double) attributeValue/Total*100;
                System.out.println("percentage :  " + percentage);
                rajreport.Cregion(session, login_region, from_date, to_date);
            }
        }
            
        }
        
        
        finally {
        	
        	collection.close();
            collection1.close();
           
        }   
        }
    public static void Cregion (IDfSession session,String login_region,String from_date,String to_date) throws DfException {
    	DfQuery dfQuery = new DfQuery();
    	DfQuery dfQuery1 = new DfQuery();
    	
    	
    	System.out.println("C region");
    	
        String dql = "select count(r_object_id) as r_object_id from cms_digidak_folder where \r\n"
        		+ "login_region='"+login_region+"' and languages in ('Hindi', 'Bilingual') and region in \r\n"
        		+ "('RO-KA',\r\n"
        		+ "'RO-WB',\r\n"
        		+ "'RO-TR',\r\n"
        		+ "'RO-TG',\r\n"
        		+ "'RO-SK',\r\n"
        		+ "'RO-OR',\r\n"
        		+ "'RO-NL',\r\n"
        		+ "'RO-MZ',\r\n"
        		+ "'RO-ML',\r\n"
        		+ "'RO-MN',\r\n"
        		+ "'RO-TN',\r\n"
        		+ "'RO-KL',\r\n"
        		+ "'RO-GA',\r\n"
        		+ "'RO-AS',\r\n"
        		+ "'RO-AR',\r\n"
        		+ "'RO-AD',\r\n"
        		+ "'RO-JK',\r\n"
        		+ "'TE-BK',\r\n"
        		+ "'TE-BM')  and decision='Outward'  and status!='Saved' and \r\n"
        		+ "r_creation_date>=DATE('"+from_date+" 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+to_date+" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
        		+ "and status != 'Saved' and is_migrated = false;";
        
        String dql2 = "select count(r_object_id) as object_id from cms_digidak_folder where \r\n"
        		+ "login_region='"+login_region+"' and languages in ('English')and region in \r\n"
        		+ "('RO-KA',\r\n"
        		+ "'RO-WB',\r\n"
        		+ "'RO-TR',\r\n"
        		+ "'RO-TG',\r\n"
        		+ "'RO-SK',\r\n"
        		+ "'RO-OR',\r\n"
        		+ "'RO-NL',\r\n"
        		+ "'RO-MZ',\r\n"
        		+ "'RO-ML',\r\n"
        		+ "'RO-MN',\r\n"
        		+ "'RO-TN',\r\n"
        		+ "'RO-KL',\r\n"
        		+ "'RO-GA',\r\n"
        		+ "'RO-AS',\r\n"
        		+ "'RO-AR',\r\n"
        		+ "'RO-AD',\r\n"
        		+ "'RO-JK',\r\n"
        		+ "'TE-BK',\r\n"
        		+ "'TE-BM') and decision='Outward'  and status!='Saved' and \r\n"
        		+ "r_creation_date>=DATE('"+from_date+" 00:00:00','mm/dd/yyyy hh:mm:ss') and r_creation_date<=DATE('"+to_date+" 23:59:59','mm/dd/yyyy hh:mm:ss')\r\n"
        		+ "and status != 'Saved' and is_migrated = false;";
     
        
        
        dfQuery.setDQL(dql);
        		dfQuery1.setDQL(dql2);
        	    
       System.out.println(dql2);
        IDfCollection collection = dfQuery.execute(session, IDfQuery.DF_READ_QUERY);
        IDfCollection collection1 = dfQuery1.execute(session, IDfQuery.DF_READ_QUERY);
        
        try {
            while (collection.next()) {
            	int attributeValue  = collection.getInt("r_object_id");
                System.out.println("Hindi/Bilingual :" + attributeValue);
            while (collection1.next()) {
            	int attributeValue1  = collection1.getInt("object_id");
                System.out.println("English :" + attributeValue1);
                int Total = (attributeValue) + (attributeValue1);
                System.out.println("Total : " +Total);
                Double percentage = (double) attributeValue/Total*100;
                System.out.println("percentage :  " + percentage);
            }
        }
            
        }
        
        
        finally {
        	
        	collection.close();
            collection1.close();
           
        }    }
}

  


	
	

	

  