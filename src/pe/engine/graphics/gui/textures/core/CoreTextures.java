package pe.engine.graphics.gui.textures.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import pe.engine.graphics.objects.Fonts;
import pe.engine.graphics.objects.Texture;
import pe.engine.graphics.objects.Texture1D;
import pe.engine.graphics.objects.Texture2D;
import pe.engine.graphics.objects.Texture3D;
import pe.engine.threading.MasterThread;
import pe.util.Util;

public class CoreTextures {

	public static final String PATH = "src/pe/engine/graphics/gui/textures/core/";
	
	public static final String PATH_DEFAULT = "";

	public static final String BACKGROUND_TEST = "BackgroundTest.png";
	public static final String FOREGROUND_TEST = "ForegroundTest.png";
	public static final String SINGLE_PIXEL_TEST = "SinglePixel.png";
	
	private static final Pattern baseHeightPattern = Pattern.compile("\\d+");
	
	public static void saveTexture(Texture image, String filePathInCore, String fileName){
		saveTexture(image, filePathInCore, fileName, "png");
	}
	
	public static void saveTexture(Texture image, String filePathInCore, String fileName, String fileExtension){
		saveTexture(image.getTexture(), filePathInCore, fileName, fileExtension);
	}
	
	public static void saveTexture(ByteBuffer image, String filePathInCore, String fileName){
		saveTexture(image, filePathInCore, fileName, "png");
	}
	
	public static void saveTexture(ByteBuffer image, String filePathInCore, String fileName, String fileExtension){
		try{
			InputStream byteInput = new ByteArrayInputStream(image.array());
			BufferedImage bi = ImageIO.read(byteInput);
			saveTexture(bi, filePathInCore, fileName, fileExtension);
		}catch(IOException e){
			MasterThread.errln("CoreTextures: Saving Texture", "An IOException occured while saving a texture from a byte buffer");
			e.printStackTrace(MasterThread.getConsoleErrStream());
		}
	}
	
	public static void saveTexture(BufferedImage image, String filePathInCore, String fileName){
		saveTexture(image, filePathInCore, fileName, "png");
	}
	
	public static void saveTexture(BufferedImage image, String filePathInCore, String fileName, String fileExtension){
		try{
			File outputfile = new File(PATH + filePathInCore + fileName + "." + fileExtension);
		    ImageIO.write(image, fileExtension, outputfile);
		}catch(IOException e){
			MasterThread.errln("CoreTextures: Saving Texture", "An IOException occured while saving a texture from a buffered image");
			e.printStackTrace(MasterThread.getConsoleErrStream());
		}
	}
	
	public static Texture2D getTexture(String filePathInCore, String filePattern){
		return getTexture(filePathInCore, filePattern, Fonts.BASE_HEIGHT_DEFAULT);
	}
	
	public static Texture2D getTexture(String filePathInCore, String filePattern, int baseHeight){
		String pt = PATH + filePathInCore + filePattern;
		pt = pt.replace("/", "\\\\");
		Pattern pattern = Pattern.compile(pt);
		File dir = Paths.get(PATH).toFile();
		String bestMatch = matchFile(pattern, dir, null, baseHeight);
		
		return bestMatch == null ? null : new Texture2D(bestMatch);
	}
	
	private static String matchFile(Pattern pattern, File directory, String bestMatch, int baseHeight){
		for(File file:directory.listFiles()){
			if(file.isDirectory())
				bestMatch = matchFile(pattern, file, bestMatch, baseHeight);
			
			String path = file.getPath();
			if(pattern.matcher(path).matches()){
				if(bestMatch == null){
					bestMatch = path;
				}else{
				
					Matcher m1 = baseHeightPattern.matcher(path);
					Matcher m0 = baseHeightPattern.matcher(bestMatch);
					if(m1.find() && m0.find()){
						int bestH = Integer.parseInt(path.substring(m0.start(), m0.end()));
						int baseH = Integer.parseInt(path.substring(m1.start(), m1.end()));
						int dist = Math.abs(baseHeight - baseH);
						
						if(dist < bestH || (dist == bestH && baseH > baseHeight))
							bestMatch = path;
					}
				}
			}
				
		}
		return bestMatch;
	}
	
	public static Texture2D getTexture(String textureName){
		return new Texture2D(PATH + textureName);
	}
}
