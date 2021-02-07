import string
from nltk.stem import PorterStemmer
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import numpy as np
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
from sklearn.model_selection import train_test_split
from imblearn.over_sampling import SMOTE


def load_data(data_set_path, label_path):

    # open files and load them into array
    with open(data_set_path, 'r', encoding="utf8") as f:
        data_set = f.read().splitlines()

    with open(label_path, 'r', encoding="utf8") as f:
        labels = f.read().splitlines()

    return data_set, labels


def process_sentence(sentence):
    # normalize and clean sentence
    ps = PorterStemmer()
    lemmatizer = WordNetLemmatizer()

    sentence = sentence.lower()
    sentence = sentence.translate(str.maketrans('', '', string.punctuation))
    sentence = sentence.split()
    sentence = [ps.stem(word) for word in sentence if not (word in set(stopwords.words('english')))]
    sentence = [lemmatizer.lemmatize(word) for word in sentence]
    sentence = " ".join(sentence)
    print(sentence)
    return sentence


def process_data(data_set):
    # create a txt file containing clean data
    data_set_clean = []

    for i in range(len(data_set)):
        print(i, "/", len(data_set))
        data_set_clean.append(process_sentence(data_set[i]))

    with open('resources/custom/dataset_clean.txt', 'w', encoding="utf8") as f:
        for item in data_set_clean:
            f.write("%s\n" % item)

    return data_set_clean


def generate_dataset():
    tokenizer = Tokenizer(num_words=5000)
    smote = SMOTE(k_neighbors=7, random_state=7777)

    data, labels = load_data("resources/custom/dataset.txt", "resources/custom/labels.txt")
    # X_train = process_data(train_data, False)

    # tokenize text to become input vector
    tokenizer.fit_on_texts(data)
    X = tokenizer.texts_to_sequences(labels)

    X = pad_sequences(X, padding='post', maxlen=500)

    X, labels = smote.fit_resample(X, labels)

    # convert to one hot
    y = convert_to_onehot(labels)

    # 70% training, 30% test
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=7777)

    # 15% test 15% validation
    # X_test, X_val, y_test, y_val = train_test_split(X_test, y_test, test_size=0.5, random_state=777)

    # val_data, y_val = load_data("resources/validation/val_dataset_clean.txt", "resources/validation/val_label.txt")
    # # X_val = process_data(val_data, False)
    #
    # tokenizer.fit_on_texts(train_data)
    # X_val = tokenizer.texts_to_sequences(val_data)
    # X_val = pad_sequences(X_val, padding='post', maxlen=200)
    #
    # y_val = convert_to_onehot(y_val)
    #
    # test_data, y_test = load_data("resources/test/test_dataset_clean.txt", "resources/test/test_label.txt")
    # # X_test = process_data(test_data, False)
    #
    # tokenizer.fit_on_texts(test_data)
    # X_test = tokenizer.texts_to_sequences(test_data)
    # X_test = pad_sequences(X_test, padding='post', maxlen=200)
    #
    # y_test = convert_to_onehot(y_test)

    # return X_train.astype(np.float32), X_val.astype(np.float32), X_test.astype(np.float32), \
    #     np.asarray(y_train).astype(np.float32), np.asarray(y_val).astype(np.float32), np.asarray(y_test).astype(np.float32), \
    #     len(tokenizer.word_index) + 1

    return X_train.astype(np.float32), X_test.astype(np.float32), \
        np.asarray(y_train).astype(np.float32), np.asarray(y_test).astype(np.float32), len(tokenizer.word_index) + 1


def convert_to_onehot(labels):
    # convert labels into one hot vector
    one_hot_vector = []

    for item in labels:
        one_hot = [0, 0]
        one_hot[int(item)] += 1
        one_hot_vector.append(one_hot)

    return one_hot_vector

