package com.alataf.learn.spring.ai.Model;

import java.util.List;

/**
 *
 * @param playerName
 * @param achievements
 */
public record Player(String playerName, List<String> achievements) {
}
