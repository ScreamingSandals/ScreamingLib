package org.screamingsandals.lib.test.utils.extensions;

import lombok.experimental.ExtensionMethod;
import org.junit.jupiter.api.Test;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Yes, this basically tests that lombok works. I just wanted to test it. At least we will know if someone breaks lombok.
 */
@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class NullableExtensionTest {
    @Test
    public void orElseThrowTest() {
        assertDoesNotThrow(() -> {
            new Object().orElseThrow();
        });

        assertDoesNotThrow(() -> {
            new Object().orElseThrow(IllegalArgumentException::new);
        });

        assertThrows(NoSuchElementException.class, () -> {
            ((Object) null).orElseThrow();
        });

        assertThrows(Exception.class, () -> {
            ((Object) null).orElseThrow(Exception::new);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ((Object) null).orElseThrow(IllegalArgumentException::new);
        });

        assertThrows(NoSuchElementException.class, () -> {
            // Based on @ExtensionMethod annotation, it should not clash, but I wanted to test it
            // (our orElseThrow method would not throw an exception in this case, because Optional itself is not null)
            Optional.ofNullable(null).orElseThrow();
        });
    }

    @Test
    public void orElseTest() {
        var nonNull = new Object();
        assertEquals(nonNull, ((Object) null).orElse(nonNull));
        assertNull(((Object) null).orElse(null));
        assertEquals(nonNull, nonNull.orElse(new Object()));
        assertEquals(nonNull, nonNull.orElse(null));
    }

    @Test
    public void orElseGetTest() {
        var nonNull = new Object();
        assertEquals(nonNull, ((Object) null).orElseGet(() -> nonNull));
        assertNull(((Object) null).orElseGet(() -> null));
        assertEquals(nonNull, nonNull.orElseGet(Object::new));
        assertEquals(nonNull, nonNull.orElseGet(() -> null));
    }

    @Test
    public void ifNotNullTest() {
        var test = new AtomicBoolean();
        ((Object) null).ifNotNull(obj -> test.set(true));
        assertFalse(test.get());

        test.set(false);
        new Object().ifNotNull(obj -> test.set(true));
        assertTrue(test.get());
    }

    @Test
    public void ifNotNullOrElseTest() {
        var test = new AtomicInteger(0);
        ((Object) null).ifNotNullOrElse(obj -> test.set(1), () -> test.set(-1));
        assertEquals(-1, test.get());

        test.set(0);
        new Object().ifNotNullOrElse(obj -> test.set(1), () -> test.set(-1));
        assertEquals(1, test.get());
    }

    @Test
    public void filterOrFalseTest() {
        assertNull(((Object) null).filterOrNull(obj -> true));
        assertNotNull(new Object().filterOrNull(obj -> true));
        assertNull(((Object) null).filterOrNull(obj -> false));
        assertNull((new Object().filterOrNull(obj -> false)));
    }

    @Test
    public void mapOrNullTest() {
        assertNull(((Object) null).mapOrNull(obj -> new Object()));
        assertNotNull(new Object().mapOrNull(obj -> new Object()));
        assertNull(((Object) null).mapOrNull(obj -> null));
        assertNull((new Object().mapOrNull(obj -> null)));
    }

    @Test
    public void oneItemStreamTest() {
        assertEquals(0, ((Object) null).oneItemStream().count());
        assertEquals(1, new Object().oneItemStream().count());
    }

    @Test
    public void toNullableTest() {
        assertNotNull(Optional.of(new Object()).toNullable());
        assertNull(Optional.empty().toNullable());
    }

    @Test
    public void toOptionalTest() {
        assertInstanceOf(Optional.class, ((Object) null).toOptional());
        assertTrue(((Object) null).toOptional().isEmpty());
        assertInstanceOf(Optional.class, new Object().toOptional());
        assertFalse(new Object().toOptional().isEmpty());
    }
}
