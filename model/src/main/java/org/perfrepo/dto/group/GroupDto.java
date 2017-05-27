package org.perfrepo.dto.group;

/**
 * Data transfer object for Group entity that represents a user group.
 *
 * @author Jiri Grunwald (grunwjir@gmail.com)
 */
public class GroupDto implements Comparable<GroupDto> {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupDto)) return false;

        GroupDto group = (GroupDto) o;

        return getName() != null ? getName().equals(group.getName()) : group.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public int compareTo(GroupDto o) {
        return this.getName().compareTo(o.getName());
    }
}
