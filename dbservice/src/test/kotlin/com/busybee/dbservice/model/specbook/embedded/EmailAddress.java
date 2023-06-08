package com.busybee.dbservice.model.specbook.embedded;

/**
 * Created with IntelliJ IDEA.
 * User: Kenan Hneide
 * Date: 10/8/13
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 *
 */

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.regex.Pattern;


@Embeddable
public class EmailAddress implements Serializable {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    @Column(name = "EMAIL")
    private String value;

    /**
     * Creates a new {@link EmailAddress} from the given string source.
     *
     * @param emailAddress must not be {@literal null} or empty.
     */
    public EmailAddress(String emailAddress) {
        Assert.isTrue(isValid(emailAddress), "Invalid email address!");
        this.value = emailAddress;
    }

    protected EmailAddress() {

    }

    /**
     * Returns whether the given {@link String} is a valid {@link EmailAddress} which means you can safely instantiate the
     * class.
     *
     * @param candidate
     * @return
     */
    public static boolean isValid(String candidate) {
        return candidate == null ? false : PATTERN.matcher(candidate).matches();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value;
    }
}

