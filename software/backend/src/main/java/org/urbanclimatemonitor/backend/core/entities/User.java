package org.urbanclimatemonitor.backend.core.entities;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User
{
	@Id
	private String email;

	private String password;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Role role;
}
