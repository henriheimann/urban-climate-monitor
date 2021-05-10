package org.urbanclimatemonitor.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.urbanclimatemonitor.backend.controller.responses.UploadResponse;
import org.urbanclimatemonitor.backend.services.UploadService;

import java.io.IOException;

@Tag(name = "upload", description = "Upload API")
@RestController
public class UploadController
{
	private final UploadService uploadService;

	public UploadController(UploadService uploadService)
	{
		this.uploadService = uploadService;
	}

	@Operation(summary = "Create an upload")
	@PostMapping("/upload")
	public UploadResponse postUpload(@RequestParam("file") MultipartFile file)
	{
		byte[] data;
		try {
			data = file.getBytes();
		} catch (IOException e) {
			throw new RuntimeException("Unable to process file upload");
		}

		return uploadService.createUpload(file.getOriginalFilename(), data);
	}

	@Operation(summary = "Get a single upload")
	@GetMapping("/upload/{id}/{filename}")
	public byte[] getUpload(@PathVariable long id, @PathVariable String filename)
	{
		return uploadService.getUpload(id, filename);
	}
}
