package com.se.matchy.metadata;

/**
 * Direct representation of the index schema at FireStore
 * Evey entry represents a top level collection or a nested sub collection
 */
public enum Collections {
    chapters, //Top Level
    serviceProviders, //Top Level
    surveys, // Top Level
    userMatches, //Top Level
    questions //Nested within Surveys collection
}
