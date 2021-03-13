package org.urbanclimatemonitor.backend.core.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "uploads")
public class Upload
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String filename;

	private byte[] data;
}
