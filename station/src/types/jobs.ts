import { z } from "zod";
import { createPagedSchema } from "@/types/helper";

const Job = z.object({
  id: z.number().int().positive(),
  title: z.string(),
  workplaceType: z.enum(["ON_SITE", "HYBRID", "REMOTE"]),
  location: z.string(),
  type: z.enum([
    "FULL_TIME",
    "PART_TIME",
    "CONTRACT",
    "TEMPORARY",
    "VOLUNTEER",
    "INTERNSHIP",
    "OTHER",
  ]),
  status: z.enum(["ACTIVE", "CLOSED", "REMOVED"]),
  postedAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
  dueDate: z.string().datetime(),
});

const PagedJob = createPagedSchema(Job);

export type Job = z.infer<typeof Job>;
export type PagedJobs = z.infer<typeof PagedJob>;
