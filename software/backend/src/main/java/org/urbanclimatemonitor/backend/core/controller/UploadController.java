package org.urbanclimatemonitor.backend.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.request.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdatePasswordDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationWithUploadsDTO;
import org.urbanclimatemonitor.backend.core.dto.result.UserDTO;
import org.urbanclimatemonitor.backend.core.services.UploadService;
import org.urbanclimatemonitor.backend.core.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "upload", description = "Upload API")
@RestController
public class UploadController
{
	private final UploadService uploadService;

	public UploadController(UploadService uploadService)
	{
		this.uploadService = uploadService;
	}

	@Operation(summary = "Get a single upload")
	@GetMapping("/upload/{id}/{filename}")
	public byte[] getUpload(@PathVariable long id, @PathVariable String filename)
	{
		return uploadService.getUpload(id, filename);
	}
}
