/**
 * Copyright (C) 2003 Jan Schäfer (jansch@users.sourceforge.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jskat.ai.nn.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jskat.ai.nn.input.GenericNetworkInputGenerator;
import org.jskat.ai.nn.util.EncogNetworkWrapper;
import org.jskat.ai.nn.util.INeuralNetwork;
import org.jskat.ai.nn.util.NetworkTopology;
import org.jskat.util.GameType;

/**
 * Holds all neural networks for the NN player.
 */
public final class SkatNetworks {

	private static int INPUT_NEURONS = GenericNetworkInputGenerator
			.getNeuronCountForAllStrategies();
	private static int HIDDEN_NEURONS = 25;
	private static int OUTPUT_NEURONS = 1;

	private static final boolean USE_BIAS = true;

	private final static SkatNetworks INSTANCE = new SkatNetworks();

	private static Map<GameType, Map<PlayerParty, INeuralNetwork>> networks;

	/**
	 * Private constructor for singleton class.
	 */
	private SkatNetworks() {

		createNetworks();
		loadNetworks();
	}

	/**
	 * Gets a neural network.
	 * 
	 * @param gameType
	 *            Game type
	 * @param isDeclarer
	 *            TRUE, if declarer network is desired
	 * @return Neural network
	 */
	public static INeuralNetwork getNetwork(GameType gameType,
			boolean isDeclarer, int trickNoInGame) {

		Map<PlayerParty, INeuralNetwork> gameTypeNets = networks.get(gameType);

		INeuralNetwork playerPartyNets = null;
		if (GameType.RAMSCH.equals(gameType) || isDeclarer) {
			playerPartyNets = gameTypeNets.get(PlayerParty.DECLARER);
		} else {
			playerPartyNets = gameTypeNets.get(PlayerParty.OPPONENT);
		}

		return playerPartyNets;
	}

	/**
	 * Gets an instance of the SkatNetworks
	 * 
	 * @return Instance
	 */
	public static SkatNetworks instance() {

		return INSTANCE;
	}

	/**
	 * Loads all neural networks from files
	 * 
	 * @param filePath
	 *            Path to files
	 */
	public static void loadNetworks() {
		for (Entry<GameType, Map<PlayerParty, INeuralNetwork>> gameTypeNets : networks
				.entrySet()) {
			for (Entry<PlayerParty, INeuralNetwork> playerPartyNet : gameTypeNets
					.getValue().entrySet()) {
				playerPartyNet.getValue().loadNetwork(
						"/org/jskat/ai/nn/data/jskat"
								.concat("." + gameTypeNets.getKey())
								.concat("." + playerPartyNet.getKey())
								.concat(".nnet"), INPUT_NEURONS,
						HIDDEN_NEURONS, OUTPUT_NEURONS);
			}
		}
	}

	/**
	 * Resets neural networks
	 */
	public static void resetNeuralNetworks() {

		createNetworks();
	}

	/**
	 * Saves all networks to files
	 * 
	 * @param path
	 *            Path to files
	 */
	public static void saveNetworks(final String path) {
		for (Entry<GameType, Map<PlayerParty, INeuralNetwork>> gameTypeNets : networks
				.entrySet()) {
			saveNetworks(path, gameTypeNets.getKey());
		}
	}

	public static void saveNetworks(final String path, GameType gameType) {

		Map<PlayerParty, INeuralNetwork> gameTypeNetworks = networks
				.get(gameType);

		for (Entry<PlayerParty, INeuralNetwork> playerPartyNet : gameTypeNetworks
				.entrySet()) {
			playerPartyNet.getValue().saveNetwork(
					path.concat("jskat").concat("." + gameType)
							.concat("." + playerPartyNet.getKey())
							.concat(".nnet"));
		}
	}

	private static void createNetworks() {
		int[] hiddenLayer = { HIDDEN_NEURONS };
		NetworkTopology topo = new NetworkTopology(INPUT_NEURONS, hiddenLayer,
				OUTPUT_NEURONS);

		networks = new HashMap<GameType, Map<PlayerParty, INeuralNetwork>>();
		for (GameType gameType : GameType.values()) {
			networks.put(gameType, new HashMap<PlayerParty, INeuralNetwork>());
			for (PlayerParty playerParty : PlayerParty.values()) {
				INeuralNetwork partyNet = new EncogNetworkWrapper(topo,
						USE_BIAS);
				networks.get(gameType).put(playerParty, partyNet);
			}
		}
	}
}
