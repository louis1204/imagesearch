package com.example.imagesearch;

public class ImageModel {
	private String title;
	private String url;
	
	public ImageModel(String title, String url)
	{
		this.title = title;
		this.url = url;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getURL()
	{
		return this.url;
	}
}
