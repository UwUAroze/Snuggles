// import {prop, getModelForClass, modelOptions} from "@typegoose/typegoose";
//
// class Attachment {
//   @prop({ required: true })
//   public url!: string;
//
//   @prop({ required: true })
//   public filename!: string;
// }
//
// @modelOptions({ schemaOptions: { collection: "userMessages" } })
// class UserMessage {
//   @prop({ required: true })
//   public content!: string;
//
//   @prop({ type: () => [Attachment], default: [] })
//   public attachments!: Attachment[];
//
//   @prop({ required: true })
//   public authorId!: string;
//
//   @prop({ required: true })
//   public channelId!: string;
//
//   @prop({ required: true })
//   public guildId!: string;
//
//   @prop({ required: true })
//   public timestamp!: Date;
// }
//
// const UserMessageModel = getModelForClass(UserMessage);
//
// export default UserMessageModel;
// export { UserMessage };