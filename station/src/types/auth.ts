import { z } from "zod";

const InUser = z.object({
  id: z.string().uuid(),
  inUrl: z.string().url(),
});

export type InUser = z.infer<typeof InUser>;
