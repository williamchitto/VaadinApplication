package com.piercey.app;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.piercey.app.entities.Account;
import com.piercey.app.entities.Permission;
import com.piercey.app.entities.Role;

public class DatabaseUtil
{
	private final static ApplicationLogger logger = new ApplicationLogger(DatabaseUtil.class);
	private final static SessionFactory sessionFactory;

	static
	{
		try
		{
			logger.trace("Initializing HibernateUtil");

			final Configuration configuration = new Configuration();
			configuration.configure();

			final ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder();

			final ServiceRegistry serviceRegistry = serviceRegistryBuilder
					.applySettings(configuration.getProperties())
					.buildServiceRegistry();

			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			logger.trace("Initializing authentication and authorization data");
			insertAuthData();
		}
		catch (Throwable e)
		{
			logger.error(e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		logger.executionTrace();
		
		if (!sessionFactory.getCurrentSession().getTransaction().isActive())
			sessionFactory.getCurrentSession().beginTransaction();
		
		return sessionFactory;
	}

	private static void insertAuthData()
	{
		logger.executionTrace();
		final Session session = getSessionFactory().getCurrentSession();
		
		try
		{
			Set<Permission> permissions = new HashSet<Permission>();
			permissions.add(insertPermission(session, "Permission 1"));
			permissions.add(insertPermission(session, "Permission 2"));
			permissions.add(insertPermission(session, "Permission 3"));
			
			Set<Role> roles = new HashSet<Role>();
			roles.add(insertRole(session, "Role 1", permissions));
			
			insertAccount(session, "admin", "admin", roles);
			
			permissions.clear();
			permissions.add(insertPermission(session, "Permission 4"));
			permissions.add(insertPermission(session, "Permission 5"));
			permissions.add(insertPermission(session, "Permission 6"));
			
			roles.clear();
			roles.add(insertRole(session, "Role 2", permissions));
			
			insertAccount(session, "guest", "guest", roles);

			permissions.clear();
			permissions.add(insertPermission(session, "Permission 7"));
			permissions.add(insertPermission(session, "Permission 8"));
			permissions.add(insertPermission(session, "Permission 9"));
			
			roles.clear();
			roles.add(insertRole(session, "Role 3", permissions));

			insertAccount(session, "member", "member", roles);
		}
		finally
		{
			session.getTransaction().commit();
		}
	}
	
	private static void insertAccount(Session session, String username, String password, Set<Role> roles)
	{
		logger.executionTrace();
		
		final Query query = session.createSQLQuery(
				"select * from Account x where x.username = :zzz")
				.addEntity(Account.class)
				.setParameter("zzz", username);

		final List<?> result = query.list();

		if (result.size() == 0)
		{
			final Account account = new Account(username, password, "buddy@waddya.at");
			final RandomNumberGenerator random = new SecureRandomNumberGenerator();

			account.setSalt(random.nextBytes().toBase64());
			account.setPassword(new Sha256Hash(account.getPassword(), account.getSalt(), 1024).toBase64());

			session.save(account);
		}
	}
	
	private static Role insertRole(Session session, String roleName, Set<Permission> permissions)
	{
		logger.executionTrace();
		
		final Query query = session.createSQLQuery(
				"select * from Role x where x.roleName = :zzz")
				.addEntity(Role.class)
				.setParameter("zzz", roleName);

		final List<?> result = query.list();

		if (result.size() > 0)
			return null;

		final Role role = new Role(roleName);
		role.setPermissions(permissions);
		
		final Serializable entityId = session.save(role);
		return (Role) session.get(Role.class, entityId);
	}
	
	private static Permission insertPermission(Session session, String permissionName)
	{
		logger.executionTrace();
		
		final Query query = session.createSQLQuery(
				"select * from Permission x where x.permissionName = :zzz")
				.addEntity(Permission.class)
				.setParameter("zzz", permissionName);

		final List<?> result = query.list();

		if (result.size() > 0)
			return null;

		final Permission permission = new Permission(permissionName);
		final Serializable entityId = session.save(permission);
		
		return (Permission) session.get(Permission.class, entityId);
	}
	
	
	
	
	
	
	public static boolean SaveAccount(Account account)
	{
		logger.executionTrace();
		
		final Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
		final Account existingAccount = getAccount(session, account.getUsername());

		if (existingAccount == null)
		{
			final RandomNumberGenerator random = new SecureRandomNumberGenerator();
			account.setSalt(random.nextBytes().toBase64());
			account.setPassword(new Sha256Hash(account.getPassword(), account.getSalt(), 1024).toBase64());
		}
		else
		{
			// Don't allow password changes during an account update.
			account.setPassword(existingAccount.getPassword());
		}

		if (!session.getTransaction().isActive())
			session.getTransaction().begin();

		session.saveOrUpdate(account);
		session.getTransaction().commit();
		
		return true;
	}

	private static Account getAccount(Session session, String username)
	{
		logger.executionTrace();

		final Query query = session.createSQLQuery(
				"select * from Account x where x.username = :zzz")
				.addEntity(Account.class)
				.setParameter("zzz", username);

		return (Account) query.uniqueResult();
	}

	
	
	
	
}
