package co.neweden.speedy;


class Factoid {
    int id;
    String factoid;
    String author;
    String response;
    MessageType type;

    Factoid() {
    }

    public int getID() {
        return this.id;
    }

    public String getFactoid() {
        return this.factoid;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getResponse() {
        return this.response;
    }

    public MessageType getMessageType() {
        return this.type;
    }
}
