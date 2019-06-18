package it.micheledallerive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SudokuFile {

	
	public static void saveFile(String actualState, String originalState, String originalSudoku){
	    JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Sudoku (.sud)", "sud");
	    chooser.setFileFilter(filter);
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	        try {
	            FileWriter fw = new FileWriter(chooser.getSelectedFile()+".sud");
	            Main.usingFile=chooser.getSelectedFile();
	            fw.write(encrypt(actualState.toString())+"\n");
	            fw.write(encrypt(originalState.toString())+"\n");
	            fw.write(encrypt(originalSudoku.toString()));
	            fw.close();
	            JOptionPane.showMessageDialog(null, "Saved", "", JOptionPane.INFORMATION_MESSAGE+JOptionPane.OK_OPTION, null);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    
	}
	
	public static void save(String actualState, File f, String originalState, String originalSudoku){
		System.out.println(f.getAbsolutePath());
		try {
            FileWriter fw = new FileWriter(f);
            fw.write(encrypt(actualState.toString())+"\n");
            fw.write(encrypt(originalState.toString())+"\n");
            fw.write(encrypt(originalSudoku.toString()));
            fw.close();
            JOptionPane.showMessageDialog(null, "Saved", "", JOptionPane.INFORMATION_MESSAGE+JOptionPane.OK_OPTION, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public static List<String> loadFile(){
		JFileChooser fileChooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Sudoku (.sud)", "sud");
	    fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    String line=null;
		    try{
		    	FileReader fileReader = new FileReader(selectedFile);
	            Main.usingFile=selectedFile;
		    	BufferedReader bufferedReader = new BufferedReader(fileReader);
		    	List<String> lines = new ArrayList<>();
		    	while((line=bufferedReader.readLine())!=null){
		    		lines.add(decrypt(line));
		    	}
		    	bufferedReader.close();
		    	return lines;
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		}
		return null;
	}
	
	private static final String ALGO = "AES";
	  private static final byte[] keyValue =
	            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

	    /**
	     * Encrypt a string with AES algorithm.
	     *
	     * @param data is a string
	     * @return the encrypted string
	     */
	    public static String encrypt(String data) throws Exception {
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encVal = c.doFinal(data.getBytes());
	        return Base64.getEncoder().encodeToString(encVal);
	    }

	    /**
	     * Decrypt a string with AES algorithm.
	     *
	     * @param encryptedData is a string
	     * @return the decrypted string
	     */
	    public static String decrypt(String encryptedData) throws Exception {
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
	        byte[] decValue = c.doFinal(decordedValue);
	        return new String(decValue);
	    }

	    /**
	     * Generate a new encryption key.
	     */
	    private static Key generateKey() throws Exception {
	        return new SecretKeySpec(keyValue, ALGO);
	    }
}
