package myfirstproject;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.io.FileInputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;


public class clearTextBox {
	
	private static WebDriver chromesdr;
	private static String sFolderPath = "D:\\Users\\sanjaykumar_g\\eclipse\\eclipseProjects";
	private static String sFileName = "MyEcel.xls";
	private static String sSheetName = "Sheet1";
	
	@Test
	public static void clearTextBox() throws Exception 
	{
		try
		{
		  ChromeOptions options = new ChromeOptions();
		  options.addArguments("--disable-notifications");
		  System.setProperty("webdriver.chrome.driver","D:\\Users\\sanjaykumar_g\\eclipse\\Lib\\chromedriver.exe");
		  
		  chromesdr = new ChromeDriver(options);
		  chromesdr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); chromesdr.get("https://www.fnp.com/control/checkout-page-rj/#/checkout-page");
		  
		   WebElement emailElement= chromesdr.findElement(By.id("loginformEmailId"));
		  
		  SendEmailFromDataDriven(emailElement,"213123123123");
	
			String sRetString = readExcel(sFolderPath, sFileName, sSheetName, emailElement);
		  
		  	SendEmailFromDataDriven(emailElement, sRetString);
		  
	      
		}
		catch(Exception ex)
		{
			System.out.print("Exception occured in Main : "+ex);
		}
		finally
		{
			chromesdr.quit();
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("taskkill /im chrome.exe /f /t");			
		}
	}
	
	public static void SendEmailFromDataDriven(WebElement emailElement, String sText)
	{
		emailElement.sendKeys(sText);
		emailElement.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
	}
	
	public static String readExcel(String folderPath,String fileName,String sheetName, WebElement oemailElement)  {
	
		String retString = "";
		try
		{
		    //Create an object of File class to open xlsx file
		    File file =    new File(folderPath+"\\"+fileName);
	
		    //Create an object of FileInputStream class to read excel file
		    FileInputStream inputStream = new FileInputStream(file);
	
		    Workbook myWorkbook = null;
	
		    //Find the file extension by splitting file name in substring  and getting only extension name
		    String fileExtensionName = fileName.substring(fileName.indexOf("."));
	
		    //Check condition if the file is xlsx file
		    if(fileExtensionName.equals(".xlsx"))
		    {
			    //If it is xlsx file then create object of XSSFWorkbook class
			    OPCPackage opcPackage = OPCPackage.open(file);
			    myWorkbook = new XSSFWorkbook(opcPackage);
		    }
		    //Check condition if the file is xls file
		    else if(fileExtensionName.equals(".xls")){
		        //If it is xls file then create object of XSSFWorkbook class
		    	//myWorkbook = new HSSFWorkbook(inputStream);
		    	myWorkbook = WorkbookFactory.create(file);
		    }
	
		    
		    //Read sheet inside the workbook by its name
		    Sheet mySheet = myWorkbook.getSheet(sheetName);
		    //Find number of rows in excel file
	
		    int rowCount = mySheet.getLastRowNum()-mySheet.getFirstRowNum();
		    //Create a loop over all the rows of excel file to read it
		    for (int i = 0; i < rowCount+1; i++) 
		    {
		        Row row = mySheet.getRow(i);

		        //Create a loop to print cell values in a row
		        for (int j = 0; j < row.getLastCellNum(); j++) 
		        {
		            //Print Excel data in console
		            System.out.print(row.getCell(j).getStringCellValue()+"|| ");
		            retString = row.getCell(j).getStringCellValue();	
		            SendEmailFromDataDriven(oemailElement, retString);
		        }
		    }
		}
		catch(Exception ex)
		{
			System.out.print("Exception occured = "+ ex.getMessage());
		}
		
		return retString;
	}
}
