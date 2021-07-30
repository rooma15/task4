package com.epam.esm.model;

import com.epam.esm.audit.auditor.AuditHelper;
import com.epam.esm.audit.model.UserAudit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
@NamedNativeQuery(name = "User.findMostUsedTag", query = """
                  select tag.id, count(tag.id) as kol from tag 
                  join certificate_tags on tag.id=certificate_tags.tag_id
                  join orders on certificate_tags.certificate_id=orders.certificate_id
                  join (select max(orders.cost) as summary, user.id as id from user 
                  join orders on user.id=orders.user_id group by user.id
                  order by summary desc
                  limit 1) as usar
                  where user_id=usar.id group by(tag.id)
                  order by kol desc
                  limit 1""")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username")
  private String username;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private List<Order> orders = new ArrayList<>();

  public User(Integer id, String firstName, String lastName, String username, List<Order> orders) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.orders = orders;
  }

  @PostPersist
  public void onPostPersist() {
    audit("INSERT");
  }

  @PostUpdate
  public void onPostUpdate() {
    audit("UPDATE");
  }

  @PostRemove
  public void onPostRemove() {
    audit("REMOVE");
  }

  private void audit(String operationType) {
    AuditHelper auditHelper = new AuditHelper();
    UserAudit user = new UserAudit();
    user.setUserId(id);
    user.setOperationTime(LocalDateTime.now());
    user.setOperationType(operationType);
    auditHelper.save(user);
  }

  public User() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) &&
            Objects.equals(firstName, user.firstName) &&
            Objects.equals(lastName, user.lastName) &&
            Objects.equals(username, user.username) &&
            Objects.equals(orders, user.orders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, username, orders);
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", username='" + username + '\'' +
            ", orders=" + orders +
            '}';
  }
}
