package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private final String name;
  private final String email;

  // standard constructors / setters / getters / toString
  public User(final String name, final String email) {
    this.name = name;
    this.email = email;
  }

  public User() {
    name = null;
    email = null;
  }

  ;

}