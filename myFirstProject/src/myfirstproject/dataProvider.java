package myfirstproject;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class dataProvider {
	public static WebDriver driver;
    public static XSSFWorkbook xlsxworkbook;
    public static HSSFWorkbook  xlsworkbook;
    public static XSSFSheet worksheet;
    public static DataFormatter formatter= new DataFormatter();  
    static String SheetName= "Sheet1";
    public static String file_location = "D:\\Users\\sanjaykumar_g\\git\\myFirstgitRepo\\myFirstProject\\readwriteexel.xlsx";
    public static String website="http://www.gcrit.com/build3/admin/";
    
    
    @BeforeTest
	 public void BeforeTest()
	 {
		 System.setProperty("webdriver.gecko.driver",
					"D:\\Users\\sanjaykumar_g\\eclipse\\Lib\\chromedriver.exe");
		  driver = new FirefoxDriver();
		  driver.get(website);
	 }
	
  
	  
	  @Test(dataProvider ="testdata")
	  public void login(String username, String password) throws InterruptedException{ 
		 
	  driver.findElement(By.name("username")).sendKeys(username);
	  driver.findElement(By.name("password")).sendKeys(password);
	  driver.findElement(By.id("tdb1")).click();
	  Thread.sleep(1000);
	  driver.findElement(By.name("username")).clear();
	  driver.findElement(By.name("password")).clear();
	  
	 }
	  
	  @AfterTest
	  public void AfterTest() throws IOException
	  {
		  driver.close();
	      Runtime rt =Runtime.getRuntime();
		  Process proc = rt.exec("taskkill /im geckodriver.exe /f /t");
			 
	  }

	  
	 
	 
	@DataProvider(name = "testdata")
    public Object [] [] readExcel() throws IOException {

		 FileInputStream fileInputStream= new FileInputStream(file_location); //Excel sheet file location get mentioned here
		 xlsxworkbook = new XSSFWorkbook (fileInputStream); //get my workbook 
	        worksheet=xlsxworkbook.getSheet(SheetName);// get my sheet from workbook
	        XSSFRow Row=worksheet.getRow(0);     //get my Row which start from 0   
	        	        
	        int RowNum = worksheet.getPhysicalNumberOfRows();// count my number of Rows
	        int ColNum= Row.getLastCellNum(); // get last ColNum 
	         
	        Object Data[][]= new Object[RowNum-1][ColNum]; // pass my  count data in array
	         
	            for(int i=0; i<RowNum-1; i++) //Loop work for Rows
	            {  
	                XSSFRow row= worksheet.getRow(i+1);
	                 
	                for (int j=0; j<ColNum; j++) //Loop work for colNum
	                {
	                    if(row==null)
	                        Data[i][j]= "";
	                    else
	                    {
	                        XSSFCell cell= row.getCell(j);
	                        if(cell==null)
	                            Data[i][j]= ""; //if it get Null value it pass no data 
	                        else
	                        {
	                            String value=formatter.formatCellValue(cell);
	                            Data[i][j]=value; //This formatter get my all values as string i.e integer, float all type data value
	                        }
	                    }
	                }
	            }
	 
	        return Data;
	    }
	}
