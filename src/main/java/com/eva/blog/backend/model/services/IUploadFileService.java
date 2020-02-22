package com.eva.blog.backend.model.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface IUploadFileService {
public Resource cargar(String nombreFoto) throws MalformedURLException;
public String copiar (MultipartFile archivo) throws IOException;
public String copiar1 (MultipartFile archivo1) throws IOException;
public String copiar2 (MultipartFile archivo2) throws IOException;

public boolean eliminar (String nombreFoto);
public Path getPath(String nombreFoto);
public Path getPath1(String nombreFoto1);
public Path getPath2(String nombreFoto2);

}
