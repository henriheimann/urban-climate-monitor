package org.urbanclimatemonitor.backend.core.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.core.entities.Upload;
import org.urbanclimatemonitor.backend.core.repositories.UploadRepository;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;

@Service
public class UploadService
{
	private final UploadRepository uploadRepository;

	public UploadService(UploadRepository uploadRepository)
	{
		this.uploadRepository = uploadRepository;
	}

	@Transactional
	public byte[] getUpload(long id, String filename)
	{
		return uploadRepository.findById(id)
				.filter(upload -> upload.getFilename().equals(filename))
				.map(Upload::getData)
				.orElseThrow(() -> new CustomLocalizedException("upload-not-found", HttpStatus.NOT_FOUND));
	}
}
