package com.redhat;

import java.util.Objects;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@RegisterForReflection
@Entity(name = "fruit")
@NamedQuery(name = "findAll", query = "SELECT b FROM fruit b")
@NamedQuery(name = "findById", query = "SELECT b FROM fruit b where b.id = :id")
public class Fruit{
  private String name;
  private int id;

  public Fruit() {
  }

  public Fruit(String _name, int _id) {
    this.name = _name;
    this.id = _id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String _name) {
    this.name = _name;
  }
  @Id
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Fruit other = (Fruit) obj;
    return Objects.equals(name, other.name) && Objects.equals(id, other.id);
  }
}
