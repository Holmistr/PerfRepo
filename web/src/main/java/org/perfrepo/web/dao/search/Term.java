package org.perfrepo.web.dao.search;

/**
 * Abstraction for atom, i.e. the tag text.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Term implements Expression {

    private String value;

    public Term(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Term{" +
                "value='" + value + '\'' +
                '}';
    }
}
