import { z } from "zod";

const Exception = z.object({
  status: z.number().int().positive(),
  message: z.string(),
  errors: z.array(
    z.object({
      field: z.string(),
      message: z.string(),
    }),
  ),
});

export type Exception = z.infer<typeof Exception>;
