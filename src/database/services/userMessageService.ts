// import UserMessageModel, {UserMessage} from "../models/userMessageModel";
//
//
// class UserMessageService {
//   public async insertUserMessage(userMessage: UserMessage): Promise<UserMessage> {
//     const loggedMessageModel = new UserMessageModel(userMessage);
//     return await loggedMessageModel.save();
//   }
//
//   public async findUserMessagesByAuthorId(authorId: string): Promise<UserMessage[]> {
//     return await UserMessageModel.find({authorId}).exec();
//   }
//
//   /* ... */
// }
//
// export default UserMessageService;
