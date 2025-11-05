# Kafka Streams – Text Cleaning Application
<div align="center">
  <img src="kafka.png" alt="Apache Kafka Logo" width="200" height="200">
</div>

This project implements a Kafka Streams application that cleans, filters, and routes text messages.

---

## 1. Topics to create

* `text-input` – input topic
* `text-clean` – valid cleaned messages
* `text-dead-letter` – invalid messages

![alt text](image.png)
![alt text](image-1.png)

---

## 2. Read messages

* Read from `text-input` topic.
* Each message is a string.

![alt text](image-2.png)

---

## 3. Clean messages

* **Trim** spaces at start/end
* Replace multiple spaces with one
* Convert to uppercase

![alt text](image-3.png)

---

## 4. Filter messages

* Reject empty or only spaces
* Reject forbidden words: HACK, SPAM, XXX
* Reject messages > 100 characters

![alt text](image-4.png)

---

## 5. Routing

* Valid -> `text-clean`
* Invalid -> `text-dead-letter`

![alt text](image-5.png)

---

## 6. Test

1. Start Kafka and topics.
2. Start the app.
3. Send messages to `text-input`.
4. Check `text-clean` and `text-dead-letter` for correct routing.

![alt text](image-6.png)
![alt text](image-7.png)
![alt text](image-8.png)
---

## Conclusion

* Shows Kafka Streams basic processing.
* Clean, filter, and route messages.
* Valid and invalid messages separated by topics.

