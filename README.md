# Open Arms
Mobile andriod app utilising AI technology to personalise support for international students

## Inspiration
With COVID-19 majorly impacting people's lives and limiting meaningful social interactions, we feel that it is more important than ever to promote positive communications online. Aside from the daily happenings found on social media, we offer a service more suited for building support connections for students. We want to create an accepting and reassuring environment for students to share and realize that they are not alone with any struggles they are facing. We hope that this project could help with students' mental health across the world, especially for students studying overseas, where communication between family and friends is more difficult. The virtual environment is accessible to counselors and mentors from anywhere in the world, so international students will be less limited by language restrictions they may face in trying to seek help in a foreign country.

We fully recognize that the app is not a perfect replacement for professional counseling. Nonetheless, we hope that it can be used to help alleviate some of the negative emotions people may feel.

## What it does
OpenArms is a mobile application that allows users to connect with others and counselors online, providing both one-on-one and group environments that encourage discussion and sharing. A personal chatroom can be made between a student and an available counselor to privately discuss possible issues they are facing or talk about life in general. An anonymous community option is also available for students to vent or share their frustrations with others, encouraging students to stand together and bond over shared experiences. Additionally, suppose a student wants to vent. In that case, an artificial chatbot is implemented to help offer minor tips for students with smaller frustrations and worries, providing an instant response to the student and alleviating pressure from counselors by reducing the number of potential messages they may receive. The artificial intelligence predicts the student's emotions based on their messages and generates an outcome based on its prediction.

## How we built it
The app was first designed using Figma and then built with Java in Android Studio, using the layout editor to create a smooth-flowing and easy to use interface. Firebase Firestore is used to store all the user data, Firebase Authentication is used to store user login details so important information can persist through daily use, and Firestore Realtime Database is used to store all the chat messages in the application. The AI was trained using a custom data set consisting of data points gathered from Reddit and Twitter posts through Keras. A CNN network was used for the model and the data points were preprocessed and normalized into valid training inputs using Python.

## Challenges we ran into
Some of the biggest challenges we ran into consisted of training the AI with a small dataset for sentiment analysis and text recognition. While the main functionality properly operates, it is unfortunate that we did not have the time to gather a larger dataset and effectively remove noisy data in order to improve the machine's accuracy. Another challenge faced was the importing of the design from Figma to Android Studio. Due to some errors in translation, the physical components needed to be manually created based on the available design.

## Accomplishments that we're proud of
In the end, we are very proud of having created an easy to use and well-functioning application that can be used straight away. It was a challenge putting everything together, but we've had the amazing opportunity to learn and bond with one another and further develop our skills as hackers.

## What we learned
We learned a ton regarding mobile app development and the use of Android Studio, followed closely behind with all of the other parts of our stack that we freshly experienced.

## What's next for OpenArms
We had big dreams for the project straight from the beginning, and even in its current state so much more can be added to it to improve its use further. The community tab is one area that we want to continue with implementation, followed by additional features such as voice chat/video call. We would also like to implement more AI applications for safety and a robust matching algorithm that can be used to pair up students with counselors with the greatest possible benefit. AI could also be implemented in the community tab to help filter out harassment and hate speech.
