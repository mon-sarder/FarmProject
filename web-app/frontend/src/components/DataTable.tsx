import React from 'react';

// A generic type for any object with a 'name' property, useful for keying
interface TitledObject {
  name: string;
  [key: string]: any;
}

// Props for the DataTable component
interface DataTableProps<T extends TitledObject> {
  columns: { key: keyof T; header: string }[];
  data: T[];
  renderRow: (item: T, index: number) => React.ReactNode;
  emptyStateMessage: string;
}

/**
 * A reusable data table component with a consistent style.
 * @param columns - An array of column definitions ({ key, header }).
 * @param data - The array of data to display.
 * @param renderRow - A function that renders a single <tr> element for a data item.
 * @param emptyStateMessage - The message to display when there is no data.
 */
export const DataTable = <T extends TitledObject>({
  columns,
  data,
  renderRow,
  emptyStateMessage,
}: DataTableProps<T>) => {
  return (
    <div className="max-h-80 overflow-auto rounded-xl border border-gray-200 dark:border-gray-800">
      <table className="w-full text-sm">
        <thead className="sticky top-0 bg-gray-50 dark:bg-zinc-900">
          <tr className="text-left">
            {columns.map((col) => (
              <th key={String(col.key)} className="px-4 py-2">
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td className="px-4 py-6 text-gray-500" colSpan={columns.length}>
                {emptyStateMessage}
              </td>
            </tr>
          ) : (
            data.map(renderRow)
          )}
        </tbody>
      </table>
    </div>
  );
};
