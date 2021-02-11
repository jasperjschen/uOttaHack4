from keras.layers import Conv1D, Dense, Dropout, Flatten, MaxPooling1D, GlobalMaxPooling1D, Embedding
from keras.layers.advanced_activations import PReLU
from keras.models import Sequential
from keras.optimizers import Adam
import data_processing
import tensorflow as tf
import matplotlib.pyplot as plt
from tensorflow import keras
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
import numpy as np
import os


def train_model(X_train, y_train, X_val, y_val, vocab_size):
    model = Sequential()
    # model.add(Convolution1D(32, 3, activation="relu", padding="same", input_shape=X_train.shape[1:]))
    # model.add(PReLU())
    # model.add(Dropout(0.1))
    # model.add(Convolution1D(64, 3, activation="relu", padding="same"))
    # model.add(PReLU())
    # model.add(Dropout(0.2))
    # model.add(MaxPooling1D(pool_size=2))
    # model.add(Dropout(0.25))
    # model.add(Flatten())

    # model.add(Dense(512, activation="relu", input_dim=X_train.shape[1]))
    # model.add(Dropout(0.5))
    # model.add(Dense(256, activation="relu"))
    # model.add(Dense(y_train.shape[0], activation="softmax"))

    model.add(Embedding(vocab_size, 50, input_length=250))
    model.add(Dropout(0.1))
    model.add(Conv1D(32, 5, activation='relu'))
    model.add(Dropout(0.2))
    model.add(GlobalMaxPooling1D())
    model.add(Dropout(0.5))
    model.add(Dense(4, activation='relu'))
    model.add(Dropout(0.5))
    model.add(Dense(y_train.shape[1], activation='softmax'))

    # Train model
    adam = Adam(lr=0.001, beta_1=0.9, beta_2=0.999, epsilon=1e-08, decay=0.0)
    model.compile(loss="binary_crossentropy", optimizer=adam, metrics=["accuracy"])
    model.summary()

    history = model.fit(
        X_train,
        y_train,
        batch_size=32,
        epochs=15,
        verbose=1,
        validation_data=(X_val, y_val),
    )

    # Serialize model
    model.save("model3.h5")

    return model, history


def eval_model(model, X, y):
    score = model.evaluate(X, y, verbose=0)
    print(f"Accuarcy: {score[1] * 100:0.2f}%")


def predict(model, data):
    prediction = model.predict(data).flatten()
    print("Prediction: ", np.argmax(prediction), "Confidence: ", prediction[np.argmax(prediction)] * 100)
    print(prediction * 100)


def plot_history(history):
    plt.style.use('ggplot')
    acc = history.history['accuracy']
    val_acc = history.history['val_accuracy']
    loss = history.history['loss']
    val_loss = history.history['val_loss']
    x = range(1, len(acc) + 1)

    plt.figure(figsize=(12, 5))
    plt.subplot(1, 2, 1)
    plt.plot(x, acc, 'b', label='Training acc')
    plt.plot(x, val_acc, 'r', label='Validation acc')
    plt.title('Training and validation accuracy')
    plt.legend()
    plt.subplot(1, 2, 2)
    plt.plot(x, loss, 'b', label='Training loss')
    plt.plot(x, val_loss, 'r', label='Validation loss')
    plt.title('Training and validation loss')
    plt.legend()

    plt.show()


def convert_to_input_vector(data):
    tokenizer = Tokenizer(num_words=5000)

    tokenizer.fit_on_texts(data)
    input_vector = tokenizer.texts_to_sequences(data)
    input_vector = pad_sequences(input_vector, padding='post', maxlen=200)

    return input_vector


if __name__ == "__main__":
    print("Num GPUs Available: ", len(tf.config.experimental.list_physical_devices('GPU')))

    X_train, X_val, y_train, y_val, vocab_size = data_processing.generate_dataset()
    print(X_train.shape, X_val.shape)
    exit()

    model, history = train_model(X_train, y_train, X_val, y_val, vocab_size)
    plot_history(history)


    # model = keras.models.load_model("model2.h5")

    sentence = ""
    while sentence != "exit":
        sentence = input("Enter a message: ")
        data = [data_processing.process_sentence(sentence)]
        input_vector = convert_to_input_vector(data)
        predict(model, input_vector)

