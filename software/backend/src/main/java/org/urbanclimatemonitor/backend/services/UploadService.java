package org.urbanclimatemonitor.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.controller.responses.UploadResponse;
import org.urbanclimatemonitor.backend.entities.Upload;
import org.urbanclimatemonitor.backend.repositories.UploadRepository;

@Service
public class UploadService
{
	private final UploadRepository uploadRepository;

	public UploadService(UploadRepository uploadRepository)
	{
		this.uploadRepository = uploadRepository;
	}

	@Transactional
	public UploadResponse createUpload(String filename, byte[] data)
	{
		Upload upload = new Upload(filename, data);
		uploadRepository.save(upload);

		return new UploadResponse(
				upload.getId(),
				upload.getFilename(),
				upload.getUrl()
		);
	}

	@Transactional
	public byte[] getUpload(long id, String filename)
	{
		return uploadRepository.findById(id)
				.filter(upload -> upload.getFilename().equals(filename))
				.map(Upload::getData)
				.orElseThrow(() -> new RuntimeException("Upload not found"));
	}
}
