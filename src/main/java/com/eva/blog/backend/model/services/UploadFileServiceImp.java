package com.eva.blog.backend.model.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import static java.nio.charset.StandardCharsets.UTF_8;




@Service
public class UploadFileServiceImp implements IUploadFileService{
	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImp.class);
	private final static String DIRECTORIO_UPLOADS="descargas";
	private final static String PROJECT_ID="pharmacyapp-b56e1";

	
	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());
		
		Resource recurso = new UrlResource(rutaArchivo.toUri());

		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error no se pudo cargar la imagen: " + nombreFoto);
		}
		
		return recurso;
	}


	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		String nombreArchivo =archivo.getOriginalFilename().replace(" ", "");
		System.out.println("3");

		Path rutaArchivo =getPath(nombreArchivo);

		log.info(rutaArchivo.toString());
		
		String	serviceAccountJson = "{  \"type\": \"service_account\",  \"project_id\": \"pharmacyapp-b56e1\",  \"private_key_id\": \"24326d74f2e7becf00e473faf46e8a4bf7d00a89\",  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDWxtKBfogFMrhO\\nBtbDWwtNgDNZxVSZfU3M4d0S5fXJJM7ZIG2YYzj13Hwz56hCkV81t1zehjtVviI4\\nBu7aWTHd6DEUbryyEW9TWpz8VtElyLNmv5EGQo9QSaLokiW2easFB9YZZEImwU94\\ns5WE9Uuk2pW5l51ds3MfieXLuzp\\/G8coSTPnMill5NshpkUk0by7\\/jZQh5u7xlxp\\nZ3ub3hK5MlPKTBQb4dvgoKtpm9jUCypI4B7F28Sl2koJ1a52RCXVf+7I2ipacWg5\\nZGg2TYdmpL2CLne6vB89SsxMgY9lIrbNgKqeYFS93eVW6K3QapqzhCGaC9RIxTXu\\nex8iLSCDAgMBAAECggEAXadWDJyBsI6U2oVV6drN+23\\/NlJ7WPDQV5pXvdFj02GH\\nBxwCXjRKFr+dsz59WwWr+C7sQqZSwklF\\/5taVQEW4xZILIo7IwTCQB0fnlM3IRw6\\nyn+XPB0TBCYP8Q0lf8PfJLLtk6XpyJ97yXy5osAZy5l7N3Ia70x8U1Dc0PFkCR9l\\nlq+VUeiM+y3fKqjMGbCjdPR+tmzVJje2g8b\\/9nzr6IOEx0tjCFRcWOreiPFLXSJw\\nSBuiy7xopOwv+Uig48P+eER1g7fh5JyADXlol\\/84IubYcG8O0vVhwtB5xYifwyT4\\n9NKQdScIFrMe0EUklONeQmAHcZJLOnHOsvE+8JoL5QKBgQD0NEWCJDhAaBFH21kW\\nXaaKgYxaXsNgFXBi02Chs8Y9aMtB8w0kNBX0KxK26cRDPdVK6WIvMExRKLMTt7T4\\nn92ILs16PjZNO0yEMoiDIvE3x+hgRYBXEnjHgAVQsXs4RkP6LE5XuCCRL0sVmZuq\\n3cnhYffD4mKzstrKSIRtBgvEhQKBgQDhJql2wlrTA+bAYNusH9Df+ED6UUpxSlZh\\nZaO0j3ea7iV50\\/vOhEXj8b31HDdX+Bjqj67JBVC0iP4ofWGLADa7kaDXUia4\\/mPI\\nt+Ay1lSsTKhLhj1Xw0rSHl25ARgg4aKIdaWICXIOlVkxYRPegGBwdKgv5DGWd7DW\\nKYy0YK2DZwKBgQC5xgoatvMUDBcZUwSyvwuy0lqzZOO8dJLKm4MngFuO2+nEgeG9\\naOJKnXnfLHbhsGhSVLkcrOFh3FRe66IAgZ1FvFUUab9lgXb0Gn0\\/RZW7mZhtpHG9\\ny4+WNQ3WFS5n4QxVhN8UXD5qNFAqXnDNy2uIyb5yhgYQLj\\/DIuebCL5rAQKBgQDb\\nzxD2\\/nwJMu\\/etTEBc\\/ZKEmXte9t\\/iyGV6NLP\\/pi4SvOPYuOdFQnepi3b1HY7jd1V\\ncn0jh+roti3bfbzzJXxJ1rz+OhfP15u3Y7ygJRt5M0XCpgXRXrn3NOovKeBSibZS\\nHvlCPGZ70lbHnIz3VsHQSDPCvbejvKqKCvpxR0le1QKBgQC8AvFHwcMHWh0LKq99\\nSvRvdZxkyL37lONEhitB764qZX\\/BxGk3bZsobLI+m4BDPb2AeC6Ud5o9tA1w1Trj\\npfWab1Rx6FDoynv7eb+5NXcPlAUDXtM0augOB4G1ZGELtBQC+0DyWTzLdalAlHvk\\nrXApNFs6CzvVQwHMAgRODUZgIw==\\n-----END PRIVATE KEY-----\\n\",  \"client_email\": \"firebase-adminsdk-oqslr@pharmacyapp-b56e1.iam.gserviceaccount.com\",  \"client_id\": \"108673189025791822167\",  \"auth_uri\": \"https:\\/\\/accounts.google.com\\/o\\/oauth2\\/auth\",  \"token_uri\": \"https:\\/\\/oauth2.googleapis.com\\/token\",  \"auth_provider_x509_cert_url\": \"https:\\/\\/www.googleapis.com\\/oauth2\\/v1\\/certs\",  \"client_x509_cert_url\": \"https:\\/\\/www.googleapis.com\\/robot\\/v1\\/metadata\\/x509\\/firebase-adminsdk-oqslr%40pharmacyapp-b56e1.iam.gserviceaccount.com\"}";


		InputStream serviceAccount = new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://pharmacyapp-b56e1.firebaseio.com")
				.setStorageBucket("pharmacyapp-b56e1.appspot.com")
				.build();

		
		FirebaseApp fireApp = FirebaseApp.initializeApp(options, UUID.randomUUID().toString() + "_" + "BLOG");
		StorageClient storageClient = StorageClient.getInstance(fireApp);

		InputStream testFile = archivo.getInputStream();
	

		String blobString = "images/" + nombreArchivo;
	System.out.println("TEST FILE="+testFile);
		
		System.out.println("BLOG="+blobString);
		storageClient.bucket("pharmacyapp-b56e1.appspot.com").create(blobString, testFile, "images/jpg");


		//Files.copy(archivo.getInputStream(), rutaArchivo);
		return nombreArchivo;
	}
	
	@Override
	public String copiar1(MultipartFile archivo1) throws IOException {
		String nombreArchivo1 = UUID.randomUUID().toString() + "_" +  archivo1.getOriginalFilename().replace(" ", "");
		System.out.println("4");

		Path rutaArchivo1 =getPath1(nombreArchivo1);

		log.info(rutaArchivo1.toString());
		String serviceAccountJson = System.getenv("SERVICE_ACCOUNT_JSON");
		InputStream serviceAccount = new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));
		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.setDatabaseUrl("https://pharmacyapp-b56e1.firebaseio.com")
			.build();
		FirebaseApp fireApp = FirebaseApp.initializeApp(options);

		StorageClient storageClient = StorageClient.getInstance(fireApp);
		InputStream testFile = new FileInputStream("blog");
		String blobString = "images/" + nombreArchivo1;

		storageClient.bucket().create(blobString, testFile , Bucket.BlobWriteOption.userProject(PROJECT_ID));

		//Files.copy(archivo1.getInputStream(), rutaArchivo1);
		return nombreArchivo1;
	}
	@Override
	public String copiar2(MultipartFile archivo2) throws IOException {
		String nombreArchivo2 = UUID.randomUUID().toString() + "_" +  archivo2.getOriginalFilename().replace(" ", "");
		System.out.println("5");

		Path rutaArchivo2 =getPath2(nombreArchivo2);

		log.info(rutaArchivo2.toString());
		String serviceAccountJson = System.getenv("SERVICE_ACCOUNT_JSON");
		InputStream serviceAccount = new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));
		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.setDatabaseUrl("https://pharmacyapp-b56e1.firebaseio.com")
			.build();
		FirebaseApp fireApp = FirebaseApp.initializeApp(options);

		StorageClient storageClient = StorageClient.getInstance(fireApp);
		InputStream testFile = new FileInputStream("blog");
		String blobString = "images/" + nombreArchivo2;

		storageClient.bucket().create(blobString, testFile , Bucket.BlobWriteOption.userProject(PROJECT_ID));

		//Files.copy(archivo2.getInputStream(), rutaArchivo2);
		return nombreArchivo2;
	}
	@Override
	public boolean eliminar(String nombreFoto) {
if(nombreFoto!=null && nombreFoto.length()>0){
	Path rutaFotoAnterior = Paths.get(DIRECTORIO_UPLOADS).resolve(nombreFoto).toAbsolutePath();
	File archivoFotoAnterior = rutaFotoAnterior.toFile();
	if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
		archivoFotoAnterior.delete();
		return true;
	}
}		
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		return Paths.get(DIRECTORIO_UPLOADS).resolve(nombreFoto).toAbsolutePath();
	}
	@Override
	public Path getPath1(String nombreFoto1) {
		return Paths.get(DIRECTORIO_UPLOADS).resolve(nombreFoto1).toAbsolutePath();
	}
	@Override
	public Path getPath2(String nombreFoto2) {
		return Paths.get(DIRECTORIO_UPLOADS).resolve(nombreFoto2).toAbsolutePath();
	}
}
