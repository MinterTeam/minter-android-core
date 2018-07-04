/*! 
 * bip39. 2018
 * 
 * \author Eduard Maximovich <edward.vstock@gmail.com>
 * \link https://github.com/edwardstock
 */

#include "Bip39Mnemonic.h"
std::vector<char *> minter::Bip39Mnemonic::getLanguages() {
    int sz = bip39_get_languages_size();
    std::vector<char *> languages(static_cast<size_t>(sz));
    bip39_get_languages(&languages[0]);

    return languages;
}
std::vector<const char *> minter::Bip39Mnemonic::getWordsFromLanguage(const char *lang) {
    words *wl[1];
    bip39_get_wordlist(lang, wl);
    if (!wl[0]) {
        return {};
    }

    std::vector<const char *> wordsList(wl[0]->len);
    for (size_t i = 0; i < wordsList.size(); i++) {
        wordsList[i] = wl[0]->indices[i];
    }

    return wordsList;
}
minter::Bip39Mnemonic::MnemonicResult minter::Bip39Mnemonic::encodeBytes(const uint8_t *src,
                                                                         const char *lang,
                                                                         size_t entropy) {
    MnemonicResult result{Ok};

    if (!validateEntropy(entropy)) {
        result.status = UnsupportedEntropy;
        return result;
    }

    struct words *wordList[1];
    bip39_get_wordlist(lang, wordList);

    char *output[1];
    bool encRes = bip39_mnemonic_from_bytes(wordList[0], src, entropy, output) == MINTER_OK;

    if (!encRes) {
        result.status = UnknownError;
        return result;
    }

    result.words = splitString(std::string(output[0]), " ");
    result.len = result.words.size();
    result.raw = std::string(output[0]);

    free(output[0]);

    return result;
}
minter::Data minter::Bip39Mnemonic::decodeMnemonic(const char *mnemonic, const char *lang, size_t entropy) {
    struct words *wordList[1];
    bip39_get_wordlist(lang, wordList);

    Data data;
    if (!validateEntropy(entropy)) {
        data.resize(0);
        return data;
    }

    data.resize(entropy);

    size_t written = 0;

    bool decRes = bip39_mnemonic_to_bytes(wordList[0], mnemonic, data.data(), entropy, &written) == MINTER_OK;
    if (decRes) {
        return data;
    }

    data.resize(0);
    return data;
}
void minter::Bip39Mnemonic::wordsToSeed(const char *words, uint8_t *out64, size_t *writtenSz) {
    bip39_mnemonic_to_seed(words, nullptr, out64, 64, writtenSz);
}
bool minter::Bip39Mnemonic::validateEntropy(size_t entropy) {
    switch (entropy) {
        case BIP39_ENTROPY_LEN_128:
        case BIP39_ENTROPY_LEN_160:
        case BIP39_ENTROPY_LEN_192:
        case BIP39_ENTROPY_LEN_224:
        case BIP39_ENTROPY_LEN_256:
        case BIP39_ENTROPY_LEN_288:
        case BIP39_ENTROPY_LEN_320:return true;
        default:break;
    }

    return false;
}
bool minter::Bip39Mnemonic::validateWords(const char *lang, const char *mnemonic) {
    words *wl[1];
    bip39_get_wordlist(lang, wl);
    if (!wl[0]) {
        return false;
    }

    return bip39_mnemonic_validate(wl[0], mnemonic) == MINTER_OK;
}
