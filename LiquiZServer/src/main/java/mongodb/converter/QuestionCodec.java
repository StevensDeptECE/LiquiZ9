/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongodb.converter;

import com.mongodb.MongoClient;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import questions.Question;

/**
 * Mongo Decoder for Items.
 */
public class QuestionCodec implements CollectibleCodec<Question> {

    private final CodecRegistry registry;
    private final Codec<Document> documentCodec;
    private final QuestionConverter converter;

    /**
     * Default constructor.
     */
    public QuestionCodec() {
        this.registry = MongoClient.getDefaultCodecRegistry();
        this.documentCodec = this.registry.get(Document.class);
        this.converter = new QuestionConverter();
    }

    /**
     * Codec constructor.
     * @param codec The existing codec to use.
     */
    public QuestionCodec(Codec<Document> codec) {
        this.documentCodec = codec;
        this.registry = MongoClient.getDefaultCodecRegistry();
        this.converter = new QuestionConverter();
    }

    /**
     * Registry constructor.
     * @param registry The CodecRegistry to use.
     */
    public QuestionCodec(CodecRegistry registry) {
        this.registry = registry;
        this.documentCodec = this.registry.get(Document.class);
        this.converter = new QuestionConverter();
    }

    /**
     * Encode the passed Item into a Mongo/BSON document.
     * @param writer The writer to use for encoding.
     * @param question The Question you want to encode
     * @param encoderContext The EncoderContext to use for encoding.
     */
    @Override
    public void encode(
                    BsonWriter writer,
                    Question question,
                    EncoderContext encoderContext
                ) {
        Document document = QuestionConverter.convert(question);

        documentCodec.encode(writer, document, encoderContext);
    }

    /**
     * Get the class that this Codec works with.
     * @return Returns the class that this Codec works with.
     */
    @Override
    public Class<Question> getEncoderClass() {
        return Question.class;
    }

    /**
     * Decodes a Mongo/BSON document into an Item.
     * @param reader The reader containing the Document.
     * @param decoderContext The DecoderContext to use for decoding.
     * @return Returns the decoded Item.
     */
    @Override
    public Question decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Question question = QuestionConverter.convert(document);

        return question;
    }

    /**
     * Generates a new ObjectId for the passed Item (if absent).
     * @param question The Question that you want generate an id for
     * @return Returns the passed Item with a new id added if there
     * was none.
     */
    @Override
    public Question generateIdIfAbsentFromDocument(Question question) {
        if (!documentHasId(question)) {
            question.setId(new ObjectId());
        }

        return question;
    }

    /**
     * Returns whether or not the passed Item has an id.
     * @param question The Question that you want to check for
     * the presence of an id.
     * @return Returns whether or not the passed Item has an id.
     */
    @Override
    public boolean documentHasId(Question question) {
        return (question.getId() != null);
    }

    /**
     * Gets the id of the passed Item.If there is no id, it will
 throw an IllegalStateException (RuntimeException).
     * @param question The Question whose id you want to get.
     * @return Returns the id of the passed Item as a BsonValue.
     */
    @Override
    public BsonValue getDocumentId(Question question)
    {
        if (!documentHasId(question)) {
            throw new IllegalStateException("The document does not contain an _id");
        }

        return new BsonString(question.getId().toHexString());
    }

}