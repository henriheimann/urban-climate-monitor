package org.urbanclimatemonitor.backend.core.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "uploads")
public class Upload
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String filename;

	private byte[] data;

	public Upload(String filename, byte[] data)
	{
		this.filename = filename;
		this.data = data;
	}
}
