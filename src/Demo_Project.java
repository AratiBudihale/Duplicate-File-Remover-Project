
import java.lang.*;
import java.util.*;
import java.io.*;
import java.io.FileInputStream;
import java.security.MessageDigest;

class Demo_Project
{
	public static void main(String args[])throws Exception
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Please enter directory name");
		String dir=br.readLine();
		
		Cleaner cobj=new Cleaner(dir);
		
		
		cobj.CleanDirectoryEmptyFile();
		cobj.CleanDirectoryDuplicateFile();
	}	
}

class Cleaner
{
	public File fdir=null;
	
	public Cleaner(String name)
	{
		//check the existance of directory
		fdir=new File(name);
		
		if(!fdir.exists())
		{
			System.out.println("Invalid dirctory name");
			System.exit(0);
		}
	}
	
	
	public void CleanDirectoryEmptyFile()
	{
		File filelist[]=fdir.listFiles();//listFiles() are used for traversal of file within directory
		int EmptyFile=0;
		
		for(File file:filelist)
		{
			if(file.length()==0)
			{
				System.out.println("Empty file name:"+file.getName());//getName() is used for getting
				                                                       //name of file
				
				if(!file.delete())
				{
					System.out.println("Unable to delete");
				}
				else
				{
					EmptyFile++;
				}
			}
		}
		System.out.println("Total empty files deleted:"+EmptyFile);
	}
	
	public void CleanDirectoryDuplicateFile()
	{
		//List all the files from Directory
		File filelist[]=fdir.listFiles();
		int DupFile=0;
		int Rcount=0;
		byte bytearr[]=new byte[1024];//badli(Bucket to read the data)
		
		//create linkedlist of string
		LinkedList<String>lobj=new LinkedList<String>();
		
		try
		{
		MessageDigest digest=MessageDigest.getInstance("MD5");
		if(digest==null)
		{
			System.out.println("Unable to get the MD5");
			System.exit(0);
		}
		
		for(File file:filelist)
		{
			//object to read the data from the file
			FileInputStream fis=new FileInputStream(file);
			
			if(file.length()!=0)
			{
				while((Rcount=fis.read(bytearr))!=-1)
				{
					digest.update(bytearr,0,Rcount);
				}
			}
			
			//To get hashbyte of checksum
			byte bytes[]=digest.digest();
			
			//editable string
			StringBuilder sb=new  StringBuilder();
			
			for(int i=0;i<bytes.length;i++)
			{
				//Add each byte from decimal to hexadecimal in StringBuilder
				sb.append(Integer.toString((bytes[i]&0xff)+0x100,16).substring(1));
			}
			
			System.out.println("File name :"+file.getName()+" checksum :"+sb);
			
			if(lobj.contains(sb.toString()))
			{
				/*if(!file.delete())
				{
					System.out.println("Unable to delete files:"+file.getName());
				}*/
				System.out.println("File get deleted:"+file.getName());
				DupFile++;
				
			}
			
			else
			{
				lobj.add(sb.toString());
			}
			fis.close();
		}	
	}
	catch(Exception obj)
	{
		System.out.println("Exception occured"+obj);
	}
	finally
	{
		
	}
	System.out.println("Total duplicate file deleted:"+DupFile);
}
}