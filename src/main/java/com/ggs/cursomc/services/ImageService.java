package com.ggs.cursomc.services;

import static com.google.common.collect.Lists.newArrayList;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.apache.commons.io.FilenameUtils.getExtension;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ggs.cursomc.services.exceptions.FileException;

@Service
public class ImageService {

	private static final String JPG = "jpg";
	private static final String PNG = "png";

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		if(!isAllowedExtension(uploadedFile))
			throw new FileException("Somente imagens JPG e PNG são permitidas");
		
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if(isPng(uploadedFile))
				img = pngToJpg(img);
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	private static boolean isAllowedExtension(MultipartFile uploadedFile) {
		String extension = extensionFile(uploadedFile);
		return extensionsAllowed().contains(extension);
	}
	
	private static ArrayList<String> extensionsAllowed() {
		return newArrayList(JPG, PNG);
	}

	private static String extensionFile(MultipartFile uploadedFile) {
		return getExtension(uploadedFile.getOriginalFilename());
	}
	
	private static boolean isPng(MultipartFile uploadedFile) {
		return PNG.equals(extensionFile(uploadedFile));
	}
	
	private static BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImg = new BufferedImage(img.getWidth(), img.getHeight(), TYPE_INT_RGB);
		jpgImg.createGraphics().drawImage(img, 0, 0, WHITE, null);
		return jpgImg;
	}
	
	public InputStream getInputStream(BufferedImage img, String extensions) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extensions, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage cropSquare(BufferedImage sourceImg) {
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		return Scalr.crop(
			sourceImg, 
			(sourceImg.getWidth()/2) - (min/2), 
			(sourceImg.getHeight()/2) - (min/2), 
			min, 
			min);		
	}

	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
	
}