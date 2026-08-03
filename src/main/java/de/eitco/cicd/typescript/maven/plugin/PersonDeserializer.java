/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Deserializes a {@link Person} from either the object form
 * ({@code { "name": ..., "email": ..., "url": ... }}) or the npm short-string form
 * ({@code "Barney Rubble <b@rubble.com> (http://barnyrubble.tumblr.com/)"}). Both forms are valid
 * in a npm {@code package.json}, so supporting the string form keeps the parser backwards compatible
 * with existing manifests.
 *
 * @see <a href="https://docs.npmjs.com/cli/v12/configuring-npm/package-json#people-fields-author-contributors">package.json people fields</a>
 */
public class PersonDeserializer extends JsonDeserializer<Person> {

    // Captures "Name <email> (url)" where email and url are optional.
    private static final Pattern SHORT_FORM =
            Pattern.compile("^\\s*([^<(]*?)\\s*(?:<([^>]*)>)?\\s*(?:\\(([^)]*)\\))?\\s*$");

    @Override
    public Person deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode node = parser.readValueAsTree();

        if (node.isTextual()) {

            return parseShortForm(node.asText());
        }

        Person result = new Person();

        if (node.hasNonNull("name")) result.setName(node.get("name").asText());
        if (node.hasNonNull("email")) result.setEmail(node.get("email").asText());
        if (node.hasNonNull("url")) result.setUrl(node.get("url").asText());

        return result;
    }

    private static Person parseShortForm(String text) {

        Person result = new Person();

        Matcher matcher = SHORT_FORM.matcher(text);

        if (matcher.matches()) {

            String name = matcher.group(1);
            String email = matcher.group(2);
            String url = matcher.group(3);

            if (name != null && !name.isEmpty()) result.setName(name);
            if (email != null && !email.isEmpty()) result.setEmail(email);
            if (url != null && !url.isEmpty()) result.setUrl(url);

        } else {

            result.setName(text);
        }

        return result;
    }
}
