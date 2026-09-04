// Skips the rest of a block comment.
static void skipBlockComment(Parser* parser)
{
  // Skip the initial '/*'.
  int nesting = 1;  // Track the nesting level of block comments.
  while (nesting > 0)
  {
    // Check for the end of the input.
    if (peekChar(parser) == '\0')
    {
      // Unterminated block comment.
      lexError(parser, "Unterminated block comment.");
      return;
    }
    // Check for nested block comments.
    if (peekChar(parser) == '/' && peekNextChar(parser) == '*')
    {
      nextChar(parser);
      nextChar(parser);
      nesting++;
      continue;
    }
    // Check for the end of the current block comment.
    if (peekChar(parser) == '*' && peekNextChar(parser) == '/')
    {
      nextChar(parser);
      nextChar(parser);
      nesting--;
      continue;
    }

    // Regular comment character.
    nextChar(parser);
  }
}