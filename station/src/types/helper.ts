import { z } from "zod";

export function createPagedSchema<Type extends z.ZodType>(schema: Type) {
  return z.object({
    content: z.array(schema),
    pageable: z.object({
      sort: z.object({
        empty: z.boolean(),
        sorted: z.boolean(),
        unsorted: z.boolean(),
      }),
      offset: z.number().int(),
      pageNumber: z.number().int(),
      pageSize: z.number().int(),
      unpaged: z.boolean(),
      paged: z.boolean(),
    }),
    last: z.boolean(),
    totalPages: z.number().int(),
    totalElements: z.number().int(),
    size: z.number().int(),
    number: z.number().int(),
    sort: z.object({
      empty: z.boolean(),
      sorted: z.boolean(),
      unsorted: z.boolean(),
    }),
    first: z.boolean(),
    numberOfElements: z.number().int(),
    empty: z.boolean(),
  });
}
