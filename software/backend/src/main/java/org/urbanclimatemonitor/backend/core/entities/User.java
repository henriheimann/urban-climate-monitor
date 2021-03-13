package org.urbanclimatemonitor.backend.core.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Role role;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_locations_with_permission", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "location_id") })
	private Set<Location> locationsWithPermission = new HashSet<>();

	public User(String email, String password, Role role, Set<Location> locationsWithPermission)
	{
		this.email = email;
		this.password = password;
		this.role = role;
		this.locationsWithPermission = locationsWithPermission;
	}
}
