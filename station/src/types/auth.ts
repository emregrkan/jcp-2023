import { z } from "zod";

const User = z.object({
  id: z.string().uuid(),
  name: z.string(),
  operator: z.boolean().default(false),
  profilePicture: z.object({
    url: z.string().url().optional(),
    width: z.number().positive().optional(),
    height: z.number().positive().optional(),
  }).optional()
});

const HrUser = User.extend({});

const InUser = z.object({
  inUrl: z.string().url().optional(),
});

export type HrUser = z.infer<typeof HrUser>;
export type InUser = z.infer<typeof InUser>;

export interface User extends HrUser, InUser { }
