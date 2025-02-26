/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package generator.domain;

import java.io.Serializable;

/**
 * @TableName account_tbl
 */
public class AccountTbl implements Serializable {
  /**
   *
   */
  private Integer id;

  /**
   *
   */
  private String userId;

  /**
   *
   */
  private Integer money;

  private static final long serialVersionUID = 1L;

  /**
   *
   */
  public Integer getId() {
    return id;
  }

  /**
   *
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   *
   */
  public String getUserId() {
    return userId;
  }

  /**
   *
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   *
   */
  public Integer getMoney() {
    return money;
  }

  /**
   *
   */
  public void setMoney(Integer money) {
    this.money = money;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (getClass() != that.getClass()) {
      return false;
    }
    AccountTbl other = (AccountTbl) that;
    return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
        && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
    result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
    result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append(" [");
    sb.append("Hash = ").append(hashCode());
    sb.append(", id=").append(id);
    sb.append(", userId=").append(userId);
    sb.append(", money=").append(money);
    sb.append(", serialVersionUID=").append(serialVersionUID);
    sb.append("]");
    return sb.toString();
  }
}
