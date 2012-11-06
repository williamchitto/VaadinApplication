package com.piercey.app.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Account", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class Account
{
	private String id;
	private String username;
	private String email;
	private String password;
	private String salt;
	private Set<Role> roles = new HashSet<Role>(0);

	public Account()
	{
	}

	public Account(String username)
	{
		setUsername(username);
	}

	public Account(String username, String password)
	{
		setUsername(username);
		setPassword(password);
	}

	public Account(String username, String password, String email)
	{
		setUsername(username);
		setPassword(password);
		setEmail(email);
	}

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "id", unique = true, nullable = false)
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Column(name = "username", unique = true, nullable = false)
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	@Column(name = "email", nullable = false)
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Column(name = "password", nullable = false)
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@Column(name = "salt", nullable = false)
	public String getSalt()
	{
		return salt;
	}

	public void setSalt(String salt)
	{
		this.salt = salt;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "AccountRole",
			joinColumns = { @JoinColumn(name = "accountId", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "roleId", nullable = false, updatable = false) })
	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}
}
