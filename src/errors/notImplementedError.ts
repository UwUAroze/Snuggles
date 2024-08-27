export class NotImplementedError extends Error {
  constructor(nameOfClass: string, prefix?: string) {
    super(
      `${prefix && `[${prefix}] `}Command ${nameOfClass} method not implemented`
    );
    this.name = "NotImplementedError";
    this.stack = (<any>new Error()).stack;
  }
}
